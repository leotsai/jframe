package org.jframe.services.impl;

import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.security.Crypto;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.User;
import org.jframe.data.enums.CaptchaUsage;
import org.jframe.data.redis.RedisApi;
import org.jframe.infrastructure.AppContext;
import org.jframe.services.CaptchaService;
import org.jframe.services.UserService;
import org.jframe.services.core.ServiceBase;
import org.jframe.services.security.UserSession;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            throw new KnownException("用户名或密码错误", "1");
        }
    }

    @Override
    public void smsLogin(String username, String smsCaptcha, CaptchaUsage usage) {
        super.useTransaction(db -> {
            User dbUser = db.getUserSet().get(username);
            if (dbUser.isDisabled()) {
                throw new KnownException("该账号已被禁用");
            }
            AppContext.getBean(CaptchaService.class).validateSmsCaptcha(username, smsCaptcha, usage);
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
    public User getUserByWeixinOpenId(String openid) {
        return super.getFromDb(db -> db.getUserSet().getByWeixinOpenId(openid));
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
                throw new KnownException("请输入验证码", "1");
            }
            String captcha1 = RedisApi.getCurrentCaptcha();
            if (!StringHelper.eq(captcha1, captcha)) {
                throw new KnownException("验证码输入错误");
            }
        }
    }

}
