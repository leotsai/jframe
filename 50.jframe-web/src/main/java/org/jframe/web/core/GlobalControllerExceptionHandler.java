package org.jframe.web.core;

import org.jframe.web.controllers.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leo on 2017-05-13.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleOther(HttpServletRequest request, HttpServletResponse response, Exception ex){
        return ErrorController.handleException(request, response, ex);
    }

}
