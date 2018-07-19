package org.jframe.web.security;

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
        chain.doFilter(new XssHttpServletRequestWrapper(req), response);
    }

    @Override
    public void destroy() {

    }

}
