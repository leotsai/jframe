package org.jframe.web.controllers;

import org.jframe.core.helpers.HttpHelper;
import org.jframe.services.UserService;
import org.jframe.web.security.Authorize;
import org.jframe.web.security.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qq
 * @date 2018/7/3
 */
@Controller("web-account-controller")
public class AccoutController extends _ControllerBase {
    public static final String ADMIN = "/admin";
    public static final String APP = "/app";

    @Autowired
    UserService service;

    @GetMapping("/login")
    @Authorize(anonymous = true)
    public ModelAndView login(String returnUrl, String code, HttpServletResponse response) throws IOException {
        if (returnUrl != null && returnUrl.startsWith(APP)) {
            response.sendRedirect("/app/login?returnUrl=" + HttpHelper.urlEncode(returnUrl) + "&code=" + (code == null ? "" : code));
            return null;
        }
        response.sendRedirect("/admin/login?returnUrl=" + HttpHelper.urlEncode(returnUrl) + "&code=" + (code == null ? "" : code));
        return null;
    }

    @GetMapping("/logout")
    @Authorize
    public ModelAndView logout(String returnUrl, HttpServletResponse response) throws IOException {
        service.logout(WebContext.getCurrent().getSession().getId());
        WebContext.getCurrent().logout();
        if (returnUrl == null) {
            response.sendRedirect("/admin");
            return null;
        }
        response.sendRedirect(returnUrl);
        return null;
    }

    @GetMapping("/register")
    @Authorize(anonymous = true)
    public ModelAndView register(String returnUrl, HttpServletResponse response) throws IOException {
        if (returnUrl != null && returnUrl.startsWith(APP)) {
            response.sendRedirect("/app/register?returnUrl=" + HttpHelper.urlEncode(returnUrl));
            return null;
        }
        response.sendRedirect("/admin/register?returnUrl=" + HttpHelper.urlEncode(returnUrl));
        return null;
    }

    @GetMapping("/findPassword")
    @Authorize(anonymous = true)
    public ModelAndView findPassword(String returnUrl, HttpServletResponse response) throws IOException {
        if (returnUrl != null && returnUrl.startsWith(APP)) {
            response.sendRedirect("/app/findPassword?returnUrl=" + HttpHelper.urlEncode(returnUrl));
            return null;
        }
        response.sendRedirect("/admin/findPassword?returnUrl=" + HttpHelper.urlEncode(returnUrl));
        return null;
    }
}
