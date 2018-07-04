package org.jframe.web.admin.controllers.account;

import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.OAuthWeixinUser;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.services.UserService;
import org.jframe.services.dto.LoginResultDto;
import org.jframe.web.admin.controllers._AdminControllerBase;
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
 * @date 2017-09-21 09:49:12
 */
@Controller("admin-login-controller")
@RequestMapping("/admin/login")
@Authorize(anonymous = true)
@WeixinAutoLogin(mode = WeixinAuthMode.OAUTH)
public class LoginController extends _AdminControllerBase {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView index(String returnUrl, String code) {
        return super.tryView("admin-account-login", () -> {
            LayoutViewModel model = new LayoutViewModel("登陆");
            model.setValue(returnUrl);
            model.setError(StringHelper.isNullOrEmpty(code) ? "" : code);
            return model;
        });
    }

    @GetMapping("/form")
    public ModelAndView form() {
        return super.tryView("app-loginForm", () -> null);
    }

    @RestPost("/doLogin")
    public StandardJsonResult doLogin(String username, String password, @RequestParam(required = false) String captcha) {
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
