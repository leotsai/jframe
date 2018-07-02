package org.jframe.web.controllers;

import org.jframe.infrastructure.AppContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Leo on 2017/11/13.
 */
@Controller("pc-res-controller")
@RequestMapping("/res")
public class ResController extends _ControllerBase {

    @GetMapping("/js-configs")
    @ResponseBody
    public String jsConfigs(HttpServletResponse response) {
        response.setContentType("text/javascript");

        String cdn = AppContext.getAppConfig().getCdnHostOfImages();
        boolean mock = AppContext.getAppConfig().isMock();
        String authCookieName = AppContext.getAppConfig().getAuthCookieName();

        return "window.appConfig={cdn:'" + cdn + "',mock:" + mock + ",authCookieName:'" + authCookieName + "'};";

    }

}
