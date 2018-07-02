package org.jframe.web.controllers;

import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * @author qq
 * @date 2018/6/26
 */
@Controller("web-home")
//@RequestMapping("/")
public class HomeController extends _ControllerBase {

//    @GetMapping
//    public ModelAndView index(HttpServletResponse response) {
//        return super.tryView("public-index", () -> {
//            LayoutViewModel model = new LayoutViewModel<>("index");
//            return model;
//        });
//    }
}
