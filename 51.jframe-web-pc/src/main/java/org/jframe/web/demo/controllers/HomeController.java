package org.jframe.web.pc.controllers;


import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Leo on 2017/1/7.
 */
@Controller("pc-home")
@RequestMapping("/")
public class HomeController extends _PcControllerBase {

    @RequestMapping("/")
    public ModelAndView index() {
        return super.tryView("pc-home-index", () -> {
            LayoutViewModel model = new LayoutViewModel();
            model.setTitle("PC home index");
            return model;
        });
    }


}
