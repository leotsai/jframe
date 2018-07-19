package org.jframe.web.app.controllers.account;

import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.OAuthWeixinUser;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.infrastructure.sms.CaptchaUsage;
import org.jframe.services.UserService;
import org.jframe.services.dto.LoginResultDto;
import org.jframe.services.utils.SmsUtil;
import org.jframe.web.app.controllers._AppControllerBase;
import org.jframe.web.app.viewModel.RegisterViewModel;
import org.jframe.web.enums.WeixinAuthMode;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WebContext;
import org.jframe.web.security.WebIdentity;
import org.jframe.web.security.WeixinAutoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author luohang
 * @date 2017-09-21 14:20:11
 */
@Controller("app-register-controller")
@RequestMapping("/app/register")
@Authorize(anonymous = true)
@WeixinAutoLogin(mode = WeixinAuthMode.OAUTH)
public class RegisterController extends _AppControllerBase {

    @Autowired
    UserService userService;

    @GetMapping
    public ModelAndView index(String returnUrl, String phone) {
        return super.tryView("app-account-register", () -> {
            RegisterViewModel model = new RegisterViewModel("注册");
            return model.buildReturnUrl(returnUrl).buildPhone(phone);
        });
    }

    @RestPost("/doRegister")
    public StandardJsonResult<LoginResultDto> doRegister(String username, String password, String smsCaptcha) {
        return super.tryJson(() -> {
            StringHelper.validate_notNullOrWhitespace(username, "手机号不能为空");
            StringHelper.validate_notNullOrWhitespace(password, "登陆密码不能为空");
            StringHelper.validate_notNullOrWhitespace(smsCaptcha, "请输入短信验证码");

            SmsUtil.validate(username, smsCaptcha, CaptchaUsage.REGISTER);
            User user = userService.register(username, password);

//            if (user.isFirstlyRegistered()) {
//                try {
//                    new BuildFansFlow(user).run();
//                } catch (KnownException e) {
//                    LogHelper.log("app.fans.create", e);
//                }
//            }
            userService.passwordLogin(username, password, "");
            WebContext.getCurrent().login(new WebIdentity(username, password));

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
