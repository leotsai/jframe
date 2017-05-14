package org.jframe.web.controllers;

import org.jframe.AppContext;
import org.jframe.infrastructure.core.KnownException;
import org.jframe.infrastructure.helpers.JsonHelper;
import org.jframe.infrastructure.helpers.MyRequestHelper;
import org.jframe.infrastructure.logging.Logger;
import org.jframe.infrastructure.web.StandardJsonResult;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leo on 2017/5/7.
 */
@Controller("web-error")
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/404")
    public ModelAndView e404(HttpServletRequest request, HttpServletResponse response){
        return this.exception(request, response);
    }


    @RequestMapping(value = "/exception")
    public ModelAndView exception(HttpServletRequest request, HttpServletResponse response) {
        String requestUri = MyRequestHelper.getRequestUri(request);
        try{
            Throwable ex = MyRequestHelper.getException(request);
            this.logError(requestUri, ex, response.getStatus());
            String message = this.getErrorMessage(ex, response.getStatus());
            if(MyRequestHelper.returnJson(request)){
                StandardJsonResult jsonResult = new StandardJsonResult();
                jsonResult.fail(message, response.getStatus());
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JsonHelper.serialize(jsonResult));
                return new ModelAndView();
            }
            return errorView(message);
        }
        catch (Exception ex){
            Logger.log("ErrorController.exception", ex);
        }
        return errorView("对不起，服务器出错了，请重试");
    }

    private ModelAndView errorView(String message){
        ModelAndView mv = new ModelAndView("pc-error");
        LayoutViewModel model = new LayoutViewModel();
        model.setError(message);
        mv.addObject("model", model);
        return mv;
    }

    private void logError(String uri, Throwable ex, int status) {
        StringBuilder sb = new StringBuilder("请求路径：" + uri);
        if (ex != null) {
            sb.append("\n错误消息：" + ex.getClass() + ":" + ex.getMessage() + "\n错误堆栈跟踪：");
            for (StackTraceElement element : ex.getStackTrace()) {
                sb.append("\n" + element);
            }
        }
        if (AppContext.printLogs()) {
            System.err.print("\n" + sb);
        }
        Logger.log("HTTP ERROR[" + status + "]", sb.toString());
    }

    private String getErrorMessage(Throwable ex, int status){
        if(ex != null) {
            if (ex.getClass().equals(KnownException.class)) {
                return ex.getMessage();
            }
            if (AppContext.showFullError()) {
                StringBuilder sb = new StringBuilder(ex.getClass() + ":" + ex.getMessage());
                sb.append("<hr/>\n<code>");
                for (StackTraceElement element : ex.getStackTrace()) {
                    sb.append("\n" + element);
                }
                sb.append("</code>");
                return sb.toString();
            }
        }

        if(status >= 500){
            return "服务器内部错误（"+status+"）";
        }
        if(status == 404){
            return "您请求的页面（或资源）不存在，请检查资源URL地址是否正确";
        }
        if(status == 401){
            return "您的权限不足，请尝试登陆或注销后重新登陆";
        }
        if(status == 403){
            return "禁止访问403";
        }
        return "未知错误";
    }



}
