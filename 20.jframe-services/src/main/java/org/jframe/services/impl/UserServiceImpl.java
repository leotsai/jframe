package org.jframe.services.impl;

import org.jframe.core.exception.KnownException;
import org.jframe.core.exception.ResultCode;
import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.http.WebClient;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.security.Crypto;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.Image;
import org.jframe.data.entities.OAuthWeixinUser;
import org.jframe.data.entities.User;
import org.jframe.data.entities.UserRoleRL;
import org.jframe.data.enums.Gender;
import org.jframe.data.sets.OAuthWeixinUserSet;
import org.jframe.infrastructure.oss.JframeOssApi;
import org.jframe.infrastructure.sms.CaptchaUsage;
import org.jframe.services.RedisApi;
import org.jframe.services.UserService;
import org.jframe.services.core.ServiceBase;
import org.jframe.services.security.UserSession;
import org.jframe.services.utils.SmsUtil;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by leo on 2017-08-23.
 */
@Service("user-service")
public class UserServiceImpl extends ServiceBase implements UserService {

    @Override
    public User get(Long id) {
        return super.getFromDb(db -> db.getUserSet().get(id));
    }

    @Override
    public User get(String username) {
        return super.getFromDb(db -> db.getUserSet().get(username));
    }

    @Override
    public User register(String username, String password) {
        try (JframeDbContext db = new JframeDbContext()) {
            User dbUser = db.getUserSet().get(username);
            String ip = HttpHelper.getIp();
            if (dbUser == null) {
                dbUser = new User(username, password);
                dbUser.setRegisterIp(ip);
            } else {
                if (!Objects.equals(Crypto.encryptWithSalt("", dbUser.getPasswordSalt()), dbUser.getPassword())) {
                    throw new KnownException("用户已注册");
                }
                dbUser.resetPassword(password);
            }
            dbUser.markLoginSuccess();
            db.save(dbUser);
            return dbUser;
        }
    }

    @Override
    public void resetPassword(String username, String password) {
        super.useTransaction(db -> {
            User user = db.getUserSet().get(username);
            if (user == null) {
                throw new KnownException("该账号不存在");
            }
            if (user.isDisabled()) {
                throw new KnownException("该账号已被禁用");
            }
            user.resetPassword(password);
            db.save(user);
        });
    }

    @Override
    public void passwordLogin(String username, String password, String captcha) {
        boolean loginSuccess = false;
        try (JframeDbContext db = new JframeDbContext()) {
            User user = db.getUserSet().get(username);
            this.validateUserAndCaptcha(user, captcha);

            if (this.isPasswordCorrect(user, password)) {
                user.markLoginSuccess();
                db.save(user);
                loginSuccess = true;
            } else {
                user.setErrorPasswordTries(user.getErrorPasswordTries() + 1);
                db.save(user);
            }
            db.commitTransaction();
        }
        if (!loginSuccess) {
            throw new KnownException(ResultCode.WRONG_PASSWORD, "用户名或密码错误");
        }
    }

    @Override
    public void smsLogin(String username, String smsCaptcha, CaptchaUsage usage) {
        super.useTransaction(db -> {
            User dbUser = db.getUserSet().get(username);
            if (dbUser.isDisabled()) {
                throw new KnownException("该账号已被禁用");
            }
            SmsUtil.validate(username, smsCaptcha, usage);
            dbUser.markLoginSuccess();
            db.save(dbUser);
        });
    }

    @Override
    public void logLogin(Long userId) {
        super.useTransaction(db -> {
            User user = db.getUserSet().get(userId);
            user.markLoginSuccess();
            db.save(user);
        });
    }

    @Override
    public void resetPassword(String username, String oldPassword, String newPassword) {
        super.useTransaction(db -> {
            User user = db.getUserSet().get(username);
            if (user == null) {
                throw new KnownException("该账号不存在");
            }
            if (user.isDisabled()) {
                throw new KnownException("该账号已被禁用");
            }
            if (!user.getPassword().equals(Crypto.encryptWithSalt(oldPassword, user.getPasswordSalt()))) {
                throw new KnownException("原密码不正确");
            }
            user.resetPassword(newPassword);
            db.save(user);
        });
    }

    @Override
    public void logout(Long userId) {
        super.useTransaction(db -> {
            User dbUser = db.getUserSet().get(userId);
            if (dbUser == null) {
                throw new KnownException("该用户不存在");
            }
            dbUser.setLoggedIn(false);
            db.save(dbUser);
        });
    }

    @Override
    public UserSession getUserSession(String username) {
        try (JframeDbContext db = new JframeDbContext()) {
            User user = db.getUserSet().get(username);
            return new UserSession(user, HttpHelper.getIp(), db.getRoleSet().getUserRoles(user.getId()));
        }
    }

    @Override
    public boolean canBindWeixin(String username, String openId) {
        try (JframeDbContext db = new JframeDbContext()) {
            User user = db.getUserSet().get(username);
            if (user == null) {
                return false;
            }
            return db.getOAuthWeixinUserSet().canBind(user.getId(), openId);
        }
    }

    @Override
    public void bindWeixin(Long userId, String openId) {
        try (JframeDbContext db = new JframeDbContext()) {
            User dbUser = db.getUserSet().get(userId);
            if (dbUser == null) {
                throw new KnownException("用户不存在");
            }
            OAuthWeixinUserSet set = db.getOAuthWeixinUserSet();
            if (null != set.getByUserId(userId)) {
                throw new KnownException("该用户已绑定微信");
            }
            OAuthWeixinUser dbWeixinUser = set.getByOpenId(openId);
            if (dbWeixinUser == null) {
                throw new KnownException("微信号不存在");
            }
            if (dbWeixinUser.getUserId() != null) {
                throw new KnownException("该微信已绑定用户");
            }
            db.beginTransaction();
            try {
                if (StringHelper.isNullOrWhitespace(dbUser.getNickname()) || dbUser.getNickname().contains("****")) {
                    String weixinNickname = dbWeixinUser.getNickname();
                    if (!StringHelper.isNullOrWhitespace(weixinNickname)) {
                        String fixedNickname = StringHelper.cleanEmoji(weixinNickname);
                        if (!StringHelper.isNullOrWhitespace(fixedNickname)) {
                            if (fixedNickname.length() > 20) {
                                fixedNickname = fixedNickname.substring(0, 20);
                            }
                            dbUser.setNickname(fixedNickname);
                        }
                    }
                }
                if (dbUser.getGender() == Gender.UNKNOWN && dbWeixinUser.getGender() != Gender.UNKNOWN) {
                    dbUser.setGender(dbWeixinUser.getGender());
                }
                if (StringHelper.isNullOrWhitespace(dbUser.getImageKey())
                        || Objects.equals(Image.Keys.UserDefaultAvatar, dbUser.getImageKey())) {
                    String wexinHeadimgUrl = dbWeixinUser.getHeadimgUrl();
                    if (!StringHelper.isNullOrWhitespace(wexinHeadimgUrl)) {
                        try (WebClient client = new WebClient()) {
                            InputStream stream = client.downloadStream(wexinHeadimgUrl);
                            String key = UUID.randomUUID().toString() + ".jpg";
                            JframeOssApi.getImages().putFile(key, stream);
                            dbUser.setImageKey(key);
                        } catch (Exception e) {
                            LogHelper.error().log("绑定微信", "下载微信头像失败：" + e.getMessage());
                        }
                    }
                }
                dbUser.setSubscribed(dbWeixinUser.isSubscribed());
                db.save(dbUser);
                dbWeixinUser.setUserId(userId);
                db.save(dbWeixinUser);
                db.commitTransaction();
            } catch (Exception e) {
                db.rollback();
                throw e;
            }
        }
    }

    @Override
    public void bindWeixin(String username, String openId) {
        super.useTransaction(db -> {
            User user = db.getUserSet().get(username);
            if (user == null) {
                throw new KnownException("用户不存在");
            }
            this.bindWeixin(user.getId(), openId);
        });
    }

    @Override
    public User getUserByWeixinOpenId(String openid) {
        return super.getFromDb(db -> db.getUserSet().getByWeixinOpenId(openid));
    }

    @Override
    public OAuthWeixinUser getWeixinUser(String openId) {
        return super.getFromDb(db -> db.getOAuthWeixinUserSet().getByOpenId(openId));
    }

    @Override
    public JList<User> getUsersByRoleId(Long roleId) {
        return super.getFromDb(db -> {
            JList<UserRoleRL> userRoleRLs = db.getUserRoleRLSet().getByRoleId(roleId);
            JList<Long> userIds = userRoleRLs.select(UserRoleRL::getUserId);
            return db.getUserSet().getAll(userIds);
        });
    }


    private boolean isPasswordCorrect(User user, String password) {
        return user.getPassword().equals(Crypto.encryptWithSalt(password, user.getPasswordSalt()));
    }

    private void validateUserAndCaptcha(User user, String captcha) {
        if (user == null) {
            throw new KnownException("该账号不存在");
        }
        if (user.isDisabled()) {
            throw new KnownException("该账号已被禁用");
        }
        if (user.getErrorPasswordTries() > 0) {
            if (StringHelper.isNullOrWhitespace(captcha)) {
                throw new KnownException(ResultCode.WRONG_PASSWORD, "请输入验证码");
            }
            String captcha1 = RedisApi.getCurrentCaptcha();
            if (!StringHelper.eq(captcha1, captcha)) {
                throw new KnownException("验证码输入错误");
            }
        }
    }

}
