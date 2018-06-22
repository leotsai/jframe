package org.jframe.core.helpers;

/**
 * Created by Leo on 2017/9/25.
 */
public class ExceptionHelper {
    public static String getFullHtmlMessage(Throwable ex) {
        if(ex == null){
            return "";
        }
        StringBuilder sb = new StringBuilder("<div class=\"ex-wrap\">");
        while (ex != null) {
            String message = ex.getMessage();
            if(!StringHelper.isNullOrWhitespace(message)){
                message = message.replace("<", "&lt;").replace(">", "&gt") + "</pre>";
            }
            sb.append(ex.getClass() + ": <pre>" + message + "</pre><br/>");
            ex = ex.getCause();
        }
        sb.append("</div>");
        return sb.toString();
    }
}
