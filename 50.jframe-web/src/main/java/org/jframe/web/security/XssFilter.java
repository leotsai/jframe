package org.jframe.web.security;

import org.jframe.infrastructure.AppContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author qq
 * @date 2018/7/16
 */
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        RequestMappingHandlerMapping handlerMapping = AppContext.getBean(RequestMappingHandlerMapping.class);
        HandlerMethod handlerMethod;
        try {
            handlerMethod = (HandlerMethod) handlerMapping.getHandler(req).getHandler();
        } catch (Exception e) {
            handlerMethod = null;
            chain.doFilter(new XssHttpServletRequestWrapper(req), response);
        }
        if (handlerMethod != null) {
            IgnoreXssFilter ignoreXssFilter = handlerMethod.getMethodAnnotation(IgnoreXssFilter.class);
            if (ignoreXssFilter == null || !ignoreXssFilter.value()) {
                chain.doFilter(new XssHttpServletRequestWrapper(req), response);
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }

}
