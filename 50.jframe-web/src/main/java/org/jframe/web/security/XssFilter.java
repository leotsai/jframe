package org.jframe.web.security;

import org.jframe.infrastructure.AppContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author qq
 * @date 2018/7/16
 */
public class XssFilter implements Filter {
    private static Pattern patterns = Pattern.compile(".(css|js|png|jpg|jpeg)$");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (patterns.matcher(req.getRequestURL().toString()).find()) {
            chain.doFilter(request, response);
            return;
        }
        RequestMappingHandlerMapping handlerMapping = AppContext.getBean(RequestMappingHandlerMapping.class);
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handlerMapping.getHandler(req).getHandler();
            IgnoreXssFilter ignoreXssFilter = handlerMethod.getMethodAnnotation(IgnoreXssFilter.class);
            if (ignoreXssFilter == null || !ignoreXssFilter.value()) {
                chain.doFilter(new XssHttpServletRequestWrapper(req), response);
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {

    }

}
