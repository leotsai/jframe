package org.jframe.core.helpers;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * Created by leo on 2017-05-13.
 */
public class RequestHelper {

    public static boolean isAjax(HttpServletRequest request) {
        return Objects.equals("XMLHttpRequest", request.getHeader("X-Requested-With"));
    }

    public static boolean isAjax() {
        return isAjax(HttpHelper.getCurrentRequest());
    }

    public static boolean isInWeixin(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        return agent != null && agent.contains("MicroMessenger");
    }

    public static boolean acceptJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.toLowerCase().contains("application/json");
    }

    public static boolean acceptHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.toLowerCase().contains("text/html");
    }

    public static boolean returnJson(HttpServletRequest request) {
        boolean returnJson;
        if (isAjax(request)) {
            returnJson = true;
            if (acceptHtml(request)) {
                returnJson = false;
            }
        } else {
            returnJson = false;
            if (acceptJson(request)) {
                returnJson = true;
            }
        }
        return returnJson;
    }

    public static String getRequestUri(HttpServletRequest request) {
        String url = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (!StringHelper.isNullOrWhitespace(url)) {
            return url;
        }
        return request.getRequestURI();
    }

    public static Throwable getException(HttpServletRequest request) {
        return (Throwable) request.getAttribute("javax.servlet.error.exception");
    }

    public static String getSessionId() {
        return HttpHelper.getCurrentRequest().getSession().getId();
    }

    public static String getRoutedDsn() {
        HttpServletRequest request = HttpHelper.getCurrentRequest();
        Map<String, Object> map = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (map != null) {
            Object dsn = map.get("dsn");
            if (dsn != null) {
                return StringUtils.split(dsn.toString(), "-")[0];
            }
        }
        return request.getParameter("dsn");
    }

}
