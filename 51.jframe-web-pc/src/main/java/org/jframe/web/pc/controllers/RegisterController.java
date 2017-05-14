package org.jframe.web.pc.controllers;


import org.jframe.infrastructure.web.StandardJsonResult;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
        model.setModel(returnUrl);
        model.setTitle("register a new user");
        return super.tryView("pc-account-register", () -> model);
    }

//    @RequestMapping(method = RequestMethod.POST)
//    @ResponseBody
//    public StandardJsonResult index(User user, boolean registerAsAdmin){
//        return super.tryJson(()->{
//            userService.register(user);
//
//            WebContext.login(user.getUsername(), user.getPassword(), true);
//        });
//    }

}
