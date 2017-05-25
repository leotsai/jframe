package org.jframe.infrastructure.helpers;

/**
 * Created by screw on 2017/5/9.
 */
public class LogHelper {

    public static void log(String group, String message) {
        try {
            System.out.print("\n【" + group + "】\n" + message);
        } catch (Exception ex) {

        }
    }

    public static void log(String group, Exception ex){
        try {
            System.out.print("\n【" + group + "】\n" + ex.getMessage());
        } catch (Exception ex2) {

        }
    }

}
