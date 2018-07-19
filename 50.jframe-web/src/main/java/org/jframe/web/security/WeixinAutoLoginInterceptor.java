package org.jframe.web.security;

import org.jframe.core.helpers.RequestHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.services.UserService;
import org.jframe.web.enums.WeixinAuthMode;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leo on 2017-09-24.
 */
public class WeixinAutoLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            WeixinAutoLogin autoLogin = this.getAutoLogin(method);
            if (autoLogin == null || autoLogin.ignored()) {
                return true;
            }
            if (RequestHelper.isInWeixin(request) == false || RequestHelper.isAjax(request)) {
                return true;
            }

            switch (autoLogin.mode()) {
                case OAUTH:
                    if (!StringHelper.isNullOrWhitespace(CookieHelper.getWeixinOpenId())) {
                        return true;
                    }
                    WxSecurityManager.redirectToWeixinAuth(request, response, WeixinAuthMode.OAUTH.name());
                    return false;
                case LOGIN:
                default:
                    if (WebContext.getCurrent().isAuthenticated()) {
                        return true;
                    }
                    if (!StringHelper.isNullOrWhitespace(CookieHelper.getWeixinOpenId())) {
                        autoLogin();
                        return true;
                    }
                    WxSecurityManager.redirectToWeixinAutoLogin(request, response, WeixinAuthMode.LOGIN.name());
                    return false;
            }
        }
        return true;
    }

    private static void autoLogin() {
        UserService service = AppContext.getBean(UserService.class);
        User user = service.getUserByWeixinOpenId(CookieHelper.getWeixinOpenId());
        if (user == null || user.isDisabled() || !user.isLoggedIn()) {
            return;
        }
        service.logLogin(user.getId());
        WebContext.getCurrent().login(new WebIdentity(user.getUsername(), ""));
    }

    private WeixinAutoLogin getAutoLogin(HandlerMethod method) {
        WeixinAutoLogin autoLogin = method.getMethodAnnotation(WeixinAutoLogin.class);
        if (autoLogin != null) {
            return autoLogin;
        }
        return method.getMethod().getDeclaringClass().getAnnotation(WeixinAutoLogin.class);
    }
}
