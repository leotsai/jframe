package org.jframe.web.app.controllers;

import org.jframe.web.security.Authorize;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author qq
 * @date 2018/6/26
 */
@Controller("app-home-controller")
@RequestMapping("/app")
@Authorize(anonymous = true)
public class AppHomeController extends _AppControllerBase {

    @RequestMapping
    public ModelAndView home() {
        return super.tryView("app-home-index", () -> {
            LayoutViewModel model = new LayoutViewModel();
            model.setTitle("home index");
            return model;
        });
    }

}
