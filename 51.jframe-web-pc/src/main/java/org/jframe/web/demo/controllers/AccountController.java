package org.jframe.web.pc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by Leo on 2017/1/8.
 */
@Controller
@RequestMapping("/account")
public class AccountController extends _PcControllerBase{





    @RequestMapping("/account/session")
    @ResponseBody
    public String index(HttpServletRequest request, HttpServletResponse response, HttpSession session){

        StringBuffer sb = new StringBuffer();
        sb.append("session.uk: "+session.getAttribute("uk") + "<br/>");
        sb.append("session.Id: " + session.getId()+ "<br/>");
        sb.append("session.CreationTime: " + session.getCreationTime()+ "<br/>");
        sb.append("session.LastAccessedTime: " + session.getLastAccessedTime()+ "<br/>");
        sb.append("session.MaxInactiveInterval: " + session.getMaxInactiveInterval()+ "<br/><hr/>");

        Enumeration<String> names = session.getAttributeNames();
        if(names.hasMoreElements()){
            String name = names.nextElement();
            while(name!=null){
                sb.append("session."+name+": " + session.getAttribute(name)+ "<br/>");
                name = names.nextElement();
            }
        }
        return sb.toString();
    }



}
