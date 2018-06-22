package org.jframe.web.pc.controllers;


import org.jframe.core.web.StandardJsonResult;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Leo on 2017/1/10.
 */
@Controller
@RequestMapping("/login")
public class LoginController extends _PcControllerBase {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(String returnUrl) {
        LayoutViewModel<String> model = new LayoutViewModel<String>();
        model.setTitle("登录");
        model.setValue(returnUrl);
        return super.tryView("pc-account-login", () -> model);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public StandardJsonResult doLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        return super.tryJson(() -> {
            //WebContext.login(username, password, true);
        });
    }



}
