package org.jframe.web.admin.controllers.account;

import org.jframe.core.exception.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author luohang
 * @date 2017-09-21 09:49:12
 */
@Controller("admin-findPassword-controller")
@RequestMapping("/admin/findPassword")
@Authorize(anonymous = true)
public class FindPasswordController extends _AdminControllerBase {

    @Autowired
    UserService userService;

    @GetMapping
    public ModelAndView index(String returnUrl) {
        return super.tryView("admin-account-findPassword", () -> {
            return null;
        });
    }

    @RestPost("/checkSmsCaptcha")
    public StandardJsonResult checkSmsCaptcha(String username, String smsCaptcha) {
        return super.tryJson(() -> {
            StringHelper.validate_notNullOrWhitespace(username, "手机号不能为空");
            StringHelper.validate_notNullOrWhitespace(smsCaptcha, "请输入短信验证码");
            SmsUtil.validate(username, smsCaptcha, CaptchaUsage.RESET_PASSWORD, true);
        });
    }

    @RestPost("/resetPassword")
    public StandardJsonResult<LoginResultDto> resetPassword(String username, String password, String smsCaptcha) {
        return super.tryJson(() -> {
            if (StringHelper.isNullOrWhitespace(username) || StringHelper.isNullOrWhitespace(password)) {
                throw new KnownException("用户名密码不能为空");
            }
            StringHelper.validate_notNullOrWhitespace(smsCaptcha, "短信验证码不能为空");

            SmsUtil.validate(username, smsCaptcha, CaptchaUsage.RESET_PASSWORD);
            userService.resetPassword(username, password);
            userService.passwordLogin(username, password, "");
            WebContext.getCurrent().login(new WebIdentity(username, password));

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
