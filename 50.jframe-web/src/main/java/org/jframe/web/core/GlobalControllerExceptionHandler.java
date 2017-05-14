package org.jframe.web.core;

import org.jframe.infrastructure.core.KnownException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by leo on 2017-05-13.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleOther(Exception ex){
        return "全局controller异常捕获，未知异常：" + ex.getMessage();
    }


    @ExceptionHandler(KnownException.class)
    @ResponseBody
    public String handleKnown(KnownException ex){
        return "全局controller异常捕获，已知异常：" + ex.getMessage();
    }

}
