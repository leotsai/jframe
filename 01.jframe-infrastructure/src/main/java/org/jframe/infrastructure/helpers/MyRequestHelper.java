package org.jframe.infrastructure.helpers;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by leo on 2017-05-13.
 */
public class MyRequestHelper {

    public static boolean isAjax(HttpServletRequest request){
        return request.getHeader("X-Requested-With") != null;
    }

    public static boolean acceptJson(HttpServletRequest request){
        String accept = request.getHeader("Accept");
        return accept != null && accept.toLowerCase().contains("application/json");
    }

    public static boolean acceptHtml(HttpServletRequest request){
        String accept = request.getHeader("Accept");
        return accept != null && accept.toLowerCase().contains("text/html");
    }

    public static boolean returnJson(HttpServletRequest request){
        boolean returnJson;
        if(isAjax(request)){
            returnJson = true;
            if(acceptHtml(request)){
                returnJson = false;
            }
        }
        else{
            returnJson = false;
            if(acceptJson(request)){
                returnJson = true;
            }
        }
        return returnJson;
    }

    public static String getRequestUri(HttpServletRequest request){
        return (String)request.getAttribute("javax.servlet.error.request_uri");
    }

    public static Throwable getException(HttpServletRequest request){
        return (Throwable)request.getAttribute("javax.servlet.error.exception");
    }

}
