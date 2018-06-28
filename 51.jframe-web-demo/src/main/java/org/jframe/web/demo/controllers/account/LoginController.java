package org.jframe.web.demo.controllers.account;


import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.RestPost;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.web.demo.controllers._DemoControllerBase;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/demo/login")
public class LoginController extends _DemoControllerBase {

    @GetMapping
    public ModelAndView index(String returnUrl) {
        LayoutViewModel<String> model = new LayoutViewModel<String>();
        model.setTitle("登录");
        model.setValue(returnUrl);
        return super.tryView("demo-account-login", () -> model);
    }

    @RestPost
    public StandardJsonResult login() {
        return super.tryJson(() -> {

        });
    }

}
