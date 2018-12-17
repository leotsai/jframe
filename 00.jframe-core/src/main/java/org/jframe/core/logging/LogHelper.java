package org.jframe.core.logging;



import org.jframe.core.helpers.ExceptionHelper;
import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.logging.enums.LogArea;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by screw on 2017/5/9.
 */
public class LogHelper {
    private final LogArea area;

    private LogHelper(LogArea area) {
        this.area = area;
    }

    public void raw(String group, String rawMessage) {
        try {
            if (rawMessage == null) {
                return;
            }
            this.tryPrintMessageIfConfigured(rawMessage);
            if (appender == null || appender.isStarted() == false) {
                return;
            }
            appender.entry(this.area, group, rawMessage);
        } catch (Throwable err) {
            this.tryPrintExceptionIfConfigured(err);
        }
    }

    public void log(String group, String message) {
        try {
            if (message == null) {
                return;
            }
            this.tryPrintMessageIfConfigured(message);
            if (appender == null || appender.isStarted() == false) {
                return;
            }
            StringBuilder sb = new StringBuilder("【" + new Date() + "】" + message + lineBreak);
            sb.append("server: " + config.getServerName() + lineBreak);
            if (config.appendHttpHeaders()) {
                this.appendHttpRequest(sb);
            }
            appender.entry(this.area, group, sb.toString());
        } catch (Throwable err) {
            this.tryPrintExceptionIfConfigured(err);
        }
    }

    public void log(String group, Throwable ex) {
        try {
            if (ex == null) {
                return;
            }
            this.tryPrintExceptionIfConfigured(ex);
            if (appender == null || appender.isStarted() == false) {
                return;
            }
            StringBuilder sb = new StringBuilder("【" + new Date() + "】" + ex.getClass().getName() + ": " + ex.getMessage() + lineBreak);
            sb.append("server: " + config.getServerName() + lineBreak);
            if (config.appendHttpHeaders()) {
                appendHttpRequest(sb);
            }
            sb.append("-------------------------------------" + lineBreak);
            sb.append(ExceptionHelper.getFullMessages(ex));
            appender.entry(this.area, group, sb.toString());
        } catch (Throwable err) {
            this.tryPrintExceptionIfConfigured(err);
        }
    }

    private void appendHttpRequest(StringBuilder sb) {
        HttpServletRequest request = HttpHelper.getCurrentRequest();
        if (request == null) {
            return;
        }
        sb.append(HttpHelper.getIp(request) + "[" + request.getMethod() + "]" + request.getRequestURI() + "?" + request.getQueryString() + lineBreak);
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            sb.append(name + ": " + request.getHeader(name) + lineBreak);
        }
    }

    public void tryPrintMessageIfConfigured(String message) {
        if (message == null || config == null) {
            return;
        }
        try {
            if (config.printStackTrace()) {
                if (this.area == LogArea.INFO) {
                    System.out.println(message);
                } else {
                    System.err.println(message);
                }
            }
        } catch (Throwable e) {

        }
    }

    public void tryPrintExceptionIfConfigured(Throwable ex) {
        if (ex == null || config == null) {
            return;
        }
        try {
            if (config.printStackTrace()) {
                if (this.area == LogArea.INFO) {
                    ex.printStackTrace(System.out);
                } else {
                    ex.printStackTrace(System.err);
                }
            }
        } catch (Throwable e) {

        }
    }

    //*****************************************************************************************//

    private final static LogHelper info = new LogHelper(LogArea.INFO);
    private final static LogHelper error = new LogHelper(LogArea.ERROR);
    private final static LogHelper fatal = new LogHelper(LogArea.FATAL);
    private static LogAppender appender;
    private static LoggingConfig config;
    private final static String lineBreak = System.getProperty("line.separator");

    public static LogHelper info() {
        return info;
    }

    public static LogHelper error() {
        return error;
    }

    public static LogHelper fatal() {
        return fatal;
    }

    public static LoggingConfig getConfig() {
        return config;
    }

    public static void startLogger(LoggingConfig config, LogAppender appenderInstance) {
        LogHelper.config = config;
        if (appenderInstance == null) {
            System.out.println("LogHelper started with a null appender that means nothing will be persisted.");
            return;
        }
        if (LogHelper.appender == null || LogHelper.appender.isStarted() == false) {
            LogHelper.appender = appenderInstance;
            LogHelper.appender.start();
            System.out.println("LogHelper started: " + appenderInstance.getClass());
        } else {
            System.out.println("LogHelper already started. Please don't call LogHelper.startLogger more than once.");
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

    public static String getLineBreak(){
        return lineBreak;
    }


}
