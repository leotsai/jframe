package org.jframe.web.security;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.helpers.RequestHelper;
import org.jframe.core.web.StandardJsonResult;
import org.jframe.data.entities.Role;
import org.jframe.core.exception.ResultCode;
import org.jframe.services.security.UserSession;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by Leo on 2017/1/8.
 */
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Authorize authorize = this.getAuthorize(method);
            if (authorize == null || authorize.anonymous()) {
                return true;
            }
            return this.authorize(request, response, authorize);
        }
        return true;
    }

    private Authorize getAuthorize(HandlerMethod method) {
        Authorize methodAuthorize = method.getMethodAnnotation(Authorize.class);
        if (methodAuthorize != null) {
            return methodAuthorize;
        }
        return method.getMethod().getDeclaringClass().getAnnotation(Authorize.class);
    }

    private boolean authorize(HttpServletRequest request, HttpServletResponse response, Authorize authorize) throws Exception {
        WebContext context = WebContext.getCurrent();
        if (context.isAuthenticated() == false) {
            this.writeResponse(ResultCode.NOT_AUTHENTICATED, request, response);
            return false;
        }
        if (authorize.visualRoles().length == 0 && authorize.permissions().length == 0 && authorize.rolesNames().length == 0) {
            return true;
        }
        Boolean authorizeResult = this.isAuthorized(context.getSession(), authorize, request, response);
        if (authorizeResult == null || authorizeResult.equals(true)) {
            return true;
        }
        this.writeResponse(ResultCode.INSUFFICIENT_PERMISSION, request, response);
        return false;
    }

    private Boolean isAuthorized(UserSession session, Authorize authorize, HttpServletRequest request, HttpServletResponse response) {
        if (authorize.visualRoles().length > 0) {
            //todo 判断
        } else if (authorize.permissions().length > 0) {
            if (session.isSuperAdmin()) {
                return true;
            }
            JList<String> permissions = JList.from(authorize.permissions());
            return permissions.any(p -> session.getAllPermissions().any(s -> s.startsWith(p)));
        } else if (authorize.rolesNames().length > 0) {
            if (session.isSuperAdmin()) {
                return true;
            }
            JList<String> roleList = JList.from(authorize.rolesNames());
            if (roleList.contains(Role.Names.ADMIN) && session.isAdmin()) {
                return true;
            }
            return roleList.any(x -> session.getRoles().any(r -> Objects.equals(x, r)));
        }
        return false;
    }

    private void writeResponse(ResultCode code, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (RequestHelper.isAjax(request)) {
            this.writeAjaxIntercept(response, code);
        } else {
            String returnUrl = HttpHelper.getPathAndQuery(request);
            response.sendRedirect("/admin/login?returnUrl=" + HttpHelper.urlEncode(returnUrl) + "&code=" + code.getCode());
        }
    }

    private void writeAjaxIntercept(HttpServletResponse response, ResultCode code) throws IOException {
        StandardJsonResult result = new StandardJsonResult();
        result.setCode(code.getCode());
        result.setSuccess(true);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().append(JsonHelper.serialize(result));
    }

}
