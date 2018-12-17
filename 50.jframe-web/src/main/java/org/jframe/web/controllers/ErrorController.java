package org.jframe.web.controllers;

import org.jframe.core.exception.KnownException;
import org.jframe.core.exception.ResultCode;
import org.jframe.core.helpers.ExceptionHelper;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.helpers.RequestHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.infrastructure.AppContext;
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
    public ModelAndView e404(HttpServletRequest request, HttpServletResponse response) {
        return this.exception(request, response);
    }


    @RequestMapping(value = "/exception")
    public ModelAndView exception(HttpServletRequest request, HttpServletResponse response) {
        return handleException(request, response, RequestHelper.getException(request));
    }

    public static ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        if (AppContext.getAppConfig().isPrintStackTrace() && ex != null) {
            ex.printStackTrace();
        }
        String requestUri = RequestHelper.getRequestUri(request);
        try {
            if (response.getStatus() == 404) {
                LogHelper.error().log("http.error.404", requestUri);
            } else {
                String fullMessage = requestUri + "\r\n<br/>" + ExceptionHelper.getFullMessages(ex);
                LogHelper.error().log("http.error." + response.getStatus(), fullMessage);
            }
            String userMessage = null;
            ResultCode resultCode = ResultCode.BUSINESS;
            if (ex == null) {
                userMessage = getUserMessage(response.getStatus());
                resultCode = ResultCode.SYSTEM;
            } else {
                if (ex instanceof KnownException) {
                    userMessage = ex.getMessage();
                    resultCode  =((KnownException) ex).getResultCode();
                } else {
                    userMessage = AppContext.getAppConfig().isTestServer() ? ExceptionHelper.getFullMessages(ex) : getUserMessage(response.getStatus());
                }
            }

            if (RequestHelper.returnJson(request)) {
                StandardJsonResult jsonResult = new StandardJsonResult();
                jsonResult.fail(resultCode, userMessage);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JsonHelper.serialize(jsonResult));
                return null;
            }
            return errorView(userMessage, requestUri);
        } catch (Exception ex2) {
            LogHelper.error().log("ErrorController.exception", ex2);
        }
        return errorView("对不起，服务器出错了，请重试", requestUri);
    }

    private static ModelAndView errorView(String message, String url) {
        String viewName = "public-error";
        LayoutViewModel model = new LayoutViewModel("出错啦");
        ModelAndView mv = new ModelAndView(viewName);
        model.setError(message);
        mv.addObject("model", model);
        return mv;
    }

    private static String getUserMessage(int status) {
        if (status >= 500) {
            return "服务器内部错误（" + status + "）";
        }
        if (status == 404) {
            return "您请求的页面（或资源）不存在，请检查资源URL地址是否正确";
        }
        if (status == 401) {
            return "您的权限不足，请尝试登陆或注销后重新登陆";
        }
        if (status == 403) {
            return "禁止访问403";
        }
        return "未知错误";
    }


}
