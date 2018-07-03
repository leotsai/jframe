package org.jframe.core.logging;

import org.jframe.core.helpers.ExceptionHelper;
import org.jframe.core.helpers.HttpHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by screw on 2017/5/9.
 */
public class LogHelper {
    private static LogAppender appender;

    public static void logRaw(String group, String rawMessage) {
        try {
            if (appender == null || appender.isStarted() == false) {
                return;
            }
            appender.entry(group, rawMessage);
        } catch (Exception ex) {
            try {
                if (appender.printStackTrace()) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {

            }
        }
    }

    public static void log(String group, String message) {
        try {
            if (appender == null || appender.isStarted() == false) {
                return;
            }
            StringBuilder sb = new StringBuilder("【" + new Date() + "】" + message);
            sb.append("\r\nserver: " + appender.getServerName() + "\r\n");
            if(appender.autoAppendHttpHeaders()){
                appendHttpRequest(sb);
            }
            appender.entry(group, sb.toString());
        } catch (Exception ex) {
            try {
                if (appender.printStackTrace()) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {

            }
        }
    }

    public static void log(String group, Throwable ex) {
        try {
            if (appender == null || appender.isStarted() == false) {
                return;
            }
            if (ex == null) {
                appender.entry(group, "");
                return;
            }
            if (appender.printStackTrace()) {
                ex.printStackTrace();
            }
            StringBuilder sb = new StringBuilder("【" + new Date() + "】" + ex.getClass().getName() + ": " + ex.getMessage() + "\r\n");
            sb.append("\r\nserver: " + appender.getServerName() + "\r\n");
            if(appender.autoAppendHttpHeaders()){
                appendHttpRequest(sb);
            }
            sb.append("-------------------------------------\r\n");
            sb.append(ExceptionHelper.getFullHtmlMessage(ex));
            appender.entry(group, sb.toString());
        } catch (Exception ex2) {
            try {
                if (appender.printStackTrace()) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {

            }
        }
    }

    private static void appendHttpRequest(StringBuilder sb) {
        HttpServletRequest request = HttpHelper.getCurrentRequest();
        if (request == null) {
            return;
        }
        sb.append(HttpHelper.getIp(request) + "[" + request.getMethod() + "]" + request.getRequestURI() + "?" + request.getQueryString() + "\r\n");
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            sb.append(name + ": " + request.getHeader(name) + "\r\n");
        }
    }

    public static void startLogger(LogAppender appenderInstance) {
        if (appender == null || appender.isStarted() == false) {
            LogHelper.appender = appenderInstance;
            appender.start();
            System.out.println("logger started");
        } else {
            System.out.println("logger already started");
        }
    }

    public static void stopLogger() {
        if (appender != null && appender.isStarted()) {
            appender.stop();
            System.out.println("logger stopped");
        } else {
            System.out.println("appender is null or not started");
        }
    }

}
