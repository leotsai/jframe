package org.jframe.infrastructure.helpers;

import org.jframe.AppContext;
import org.jframe.infrastructure.logging.FileLogger;
import org.jframe.infrastructure.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by screw on 2017/5/9.
 */
public class LogHelper {

    private final static Logger logger = new FileLogger();


    public static void log(String group, String message) {
        try {
            StringBuilder sb = new StringBuilder("【" + new Date() + "】" + message);
            appendHttpRequest(sb);
            logger.entry(group, sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void log(String group, Exception ex) {
        try {
            StringBuilder sb = new StringBuilder("【" + new Date() + "】" + ex.getClass().getName() + ": " + ex.getMessage() + "\r\n");
            appendHttpRequest(sb);
            sb.append("-------------------------------------\r\n");
            for (StackTraceElement element : ex.getStackTrace()) {
                sb.append(element.toString() + "\r\n");
            }
            logger.entry(group, sb.toString());
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }

    private static void appendHttpRequest(StringBuilder sb) {
        HttpServletRequest request = AppContext.getCurrentRequest();
        if (request == null) {
            return;
        }
        sb.append(request.getRemoteAddr() + "[" + request.getMethod() + "]" + request.getRequestURI() + "?" + request.getQueryString() + "\r\n");
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            sb.append(name + ": " + request.getHeader(name) + "\r\n");
        }
    }

    public static void startLogger(){
        logger.start();
    }

    public static void stopLogger(){
        logger.stop();
    }

}
