package org.jframe.services;

import org.jframe.data.entities.User;
import org.jframe.data.enums.CaptchaUsage;
import org.jframe.services.security.UserSession;

/**
 * Created by leo on 2017-08-22.
 */
public interface UserService {
    User get(Long id);

    User get(String username);

    void passwordLogin(String username, String password, String captcha);

    void smsLogin(String username, String smsCaptcha, CaptchaUsage usage);

    void logLogin(Long userId);

    User register(String username, String password);

    void resetPassword(String username, String oldPassword, String newPassword);

    void logout(Long userId);

    UserSession getUserSession(String username);

    User getUserByWeixinOpenId(String openid);
}
