package org.jframe.web.demo.controllers.account;

import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.OAuthWeixinUser;
import org.jframe.data.entities.User;
import org.jframe.data.enums.CaptchaUsage;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.services.CaptchaService;
import org.jframe.services.UserService;
import org.jframe.services.dto.LoginResultDto;
import org.jframe.web.demo.controllers._DemoControllerBase;
import org.jframe.web.enums.WeixinAuthMode;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WebContext;
import org.jframe.web.security.WebIdentity;
import org.jframe.web.security.WeixinAutoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author luohang
 * @date 2017-09-21 09:49:12
 */
@Controller("app-smsLogin-controller")
@RequestMapping("/demo/smsLogin")
@Authorize(anonymous = true)
@WeixinAutoLogin(mode = WeixinAuthMode.OAUTH)
public class SmsLoginController extends _DemoControllerBase {

    @Autowired
    UserService userService;

    @Autowired
    CaptchaService captchaService;

    @RestPost("/doLogin")
    public StandardJsonResult<LoginResultDto> doLogin(String username, String smsCaptcha) {
        return super.tryJson(() -> {
            if (StringHelper.isNullOrWhitespace(username) || StringHelper.isNullOrWhitespace(smsCaptcha)) {
                throw new KnownException("请输入手机及短信验证码");
            }

            if (userService.get(username) == null) {
                captchaService.validateSmsCaptcha(username, smsCaptcha, CaptchaUsage.SMS_LOGIN);
                captchaService.markLatestCaptchaUnused(username, CaptchaUsage.SMS_LOGIN, smsCaptcha);
                User user = userService.register(username, "");
            }

            userService.smsLogin(username, smsCaptcha, CaptchaUsage.SMS_LOGIN);
            WebContext.getCurrent().login(new WebIdentity(username, ""));

            String openId = CookieHelper.getWeixinOpenId();
            if (!StringHelper.isNullOrWhitespace(openId) && userService.canBindWeixin(username, openId)) {
                userService.bindWeixin(username, openId);
                OAuthWeixinUser weixinUser = userService.getWeixinUser(openId);
                return new LoginResultDto(weixinUser);
            }
            return new LoginResultDto();
        });
    }

}
