package org.jframe.web.api.controllers;

import org.jframe.AppContext;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.web.StandardJsonResult;
import org.jframe.services.api.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by leo on 2017-05-31.
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends _ApiControllerBase{


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public StandardJsonResult index(User user){
        return super.tryJson(()->{
            UserService service = AppContext.getBean(UserService.class);
            service.register(user);
        });
    }
}
