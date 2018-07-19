package org.jframe.web.admin.controllers;

import org.jframe.web.admin.Menu;
import org.jframe.web.admin.score.AdminLayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author qq
 * @date 2018/6/26
 */
@Controller("admin-home-controller")
@RequestMapping("/admin")
public class AdminHomeController extends _AdminControllerBase {

    @RequestMapping
    public ModelAndView home() {
        return super.tryView("admin-home-index", () -> {
            AdminLayoutViewModel model = new AdminLayoutViewModel();
            model.setTitle("home index");
            model.setValue("&lt;script&gt;alert('111')</script>");
            model.setCurrentPage(Menu.home());
            return model;
        });
    }

}
