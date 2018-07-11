package org.jframe.web.admin.controllers.account;

import org.jframe.core.exception.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.sms.CaptchaUsage;
import org.jframe.services.UserService;
import org.jframe.services.dto.LoginResultDto;
import org.jframe.services.utils.SmsUtil;
import org.jframe.web.admin.controllers._AdminControllerBase;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WebContext;
import org.jframe.web.security.WebIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author luohang
 * @date 2017-09-21 09:49:12
 */
@Controller("admin-smsLogin-controller")
@RequestMapping("/admin/smsLogin")
@Authorize(anonymous = true)
public class SmsLoginController extends _AdminControllerBase {

    @Autowired
    UserService userService;

    @RestPost("/doLogin")
    public StandardJsonResult<LoginResultDto> doLogin(String username, String smsCaptcha) {
        return super.tryJson(() -> {
            if (StringHelper.isNullOrWhitespace(username) || StringHelper.isNullOrWhitespace(smsCaptcha)) {
                throw new KnownException("请输入手机及短信验证码");
            }

            if (userService.get(username) == null) {
                SmsUtil.validate(username, smsCaptcha, CaptchaUsage.SMS_LOGIN, true);
                User user = userService.register(username, "");
            }

            userService.smsLogin(username, smsCaptcha, CaptchaUsage.SMS_LOGIN);
            WebContext.getCurrent().login(new WebIdentity(username, ""));

//            String openId = CookieHelper.getWeixinOpenId();
//            if (!StringHelper.isNullOrWhitespace(openId) && userService.canBindWeixin(username, openId)) {
//                userService.bindWeixin(username, openId);
//                OAuthWeixinUser weixinUser = userService.getWeixinUser(openId);
//                return new LoginResultDto(weixinUser);
//            }
            return new LoginResultDto();
        });
    }

}
