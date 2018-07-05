package org.jframe.web.app.controllers.account;

import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.OAuthWeixinUser;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.services.UserService;
import org.jframe.services.dto.LoginResultDto;
import org.jframe.web.app.controllers._AppControllerBase;
import org.jframe.web.app.viewModel.LoginViewModel;
import org.jframe.web.enums.WeixinAuthMode;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WebContext;
import org.jframe.web.security.WebIdentity;
import org.jframe.web.security.WeixinAutoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author luohang
 * @date 2017-09-21 09:49:12
 */
@Controller("app-login-controller")
@RequestMapping("/app/login")
@Authorize(anonymous = true)
@WeixinAutoLogin(mode = WeixinAuthMode.OAUTH)
public class LoginController extends _AppControllerBase {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView index(String returnUrl) {
        return super.tryView("app-account-login", () -> {
            LoginViewModel model = new LoginViewModel().build(returnUrl);
            return model;
        });
    }

    @GetMapping("/form")
    public ModelAndView form() {
        return super.tryView("app-loginForm", () -> null);
    }

    @RestPost("/doLogin")
    public StandardJsonResult<LoginResultDto> doLogin(String username, String password, @RequestParam(required = false) String captcha, @RequestParam(required = false) String password2) {
        return super.tryJson(() -> {
            if (StringHelper.isNullOrWhitespace(username) || StringHelper.isNullOrWhitespace(password)) {
                throw new KnownException("请输入用户名密码");
            }
            userService.passwordLogin(username, password, captcha);
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
