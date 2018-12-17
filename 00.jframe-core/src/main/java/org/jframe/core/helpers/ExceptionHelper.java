package org.jframe.core.helpers;

import org.jframe.core.logging.LogHelper;

/**
 * Created by Leo on 2017/9/25.
 */
public class ExceptionHelper {
    public static String getErrorLine(Throwable ex) {
        StackTraceElement[] stacks = ex.getStackTrace();
        if (stacks == null || stacks.length == 0) {
            return null;
        }
        StackTraceElement trace = stacks[0];
        return trace.getClassName() + "." + trace.getMethodName() + ". line: " + trace.getLineNumber();
    }

    public static String getFullMessages(Throwable ex) {
        if (ex == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String errorLine = getErrorLine(ex);
        if (errorLine != null) {
            sb.append(errorLine + LogHelper.getLineBreak());
        }
        while (ex != null) {
            sb.append(ex.getClass() + ": " + ex.getMessage() + LogHelper.getLineBreak());
            ex = ex.getCause();
        }
        return sb.toString();
    }
}
