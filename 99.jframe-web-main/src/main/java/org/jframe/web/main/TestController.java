package org.jframe.web.main;


import org.jframe.infrastructure.core.KnownException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by leo on 2017/5/8.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/index")
    @ResponseBody
    public String index() throws Exception{
        //org.springframework.orm.hibernate4.LocalSessionFactoryBean
        //org.slf4j.impl.StaticLoggerBinder
        throw new Exception("test error index");
    }

    @RequestMapping("/error")
    public ModelAndView jsp(){
        ModelAndView mv = new ModelAndView("/error.jsp");
        return mv;
    }

}
