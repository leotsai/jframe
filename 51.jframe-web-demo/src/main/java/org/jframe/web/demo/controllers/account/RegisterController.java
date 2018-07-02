package org.jframe.web.demo.controllers.account;

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
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author luohang
 * @date 2017-09-21 14:20:11
 */
@Controller("app-register-controller")
@RequestMapping("/demo/register")
@Authorize(anonymous = true)
@WeixinAutoLogin(mode = WeixinAuthMode.OAUTH)
public class RegisterController extends _DemoControllerBase {

    @Autowired
    UserService userService;

    @Autowired
    CaptchaService captchaService;


    @GetMapping
    public ModelAndView index(String returnUrl) {
        return super.tryView("demo-account-register", () -> {
            LayoutViewModel model = new LayoutViewModel("注册", returnUrl);
            return model;
        });
    }

    @RestPost("/doRegister")
    public StandardJsonResult<LoginResultDto> doRegister(String username, String password, String smsCaptcha) {
        return super.tryJson(() -> {
            StringHelper.validate_notNullOrWhitespace(username, "手机号不能为空");
            StringHelper.validate_notNullOrWhitespace(password, "登陆密码不能为空");
            StringHelper.validate_notNullOrWhitespace(smsCaptcha, "请输入短信验证码");

            captchaService.validateSmsCaptcha(username, smsCaptcha, CaptchaUsage.REGISTER);
            User user = userService.register(username, password);

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
