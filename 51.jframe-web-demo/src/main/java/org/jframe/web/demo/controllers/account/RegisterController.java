package org.jframe.web.demo.controllers.account;

import org.jframe.web.demo.controllers._DemoControllerBase;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Leo on 2017/1/10.
 */
@Controller
@RequestMapping("/demo/register")
public class RegisterController extends _DemoControllerBase {

    @GetMapping
    public ModelAndView index(String returnUrl) {
        LayoutViewModel model = new LayoutViewModel();
        model.setValue(returnUrl);
        model.setTitle("register a new user");
        return super.tryView("demo-account-register", () -> model);
    }


}
