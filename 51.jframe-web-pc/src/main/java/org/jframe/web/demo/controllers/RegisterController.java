package org.jframe.web.pc.controllers;


import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Leo on 2017/1/10.
 */
@Controller
@RequestMapping("/register")
public class RegisterController extends  _PcControllerBase{

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(String returnUrl){
        LayoutViewModel model = new LayoutViewModel();
        model.setValue(returnUrl);
        model.setTitle("register a new user");
        return super.tryView("pc-account-register", () -> model);
    }


}
