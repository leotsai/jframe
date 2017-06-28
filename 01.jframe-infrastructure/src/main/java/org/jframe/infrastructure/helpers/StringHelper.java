package org.jframe.infrastructure.helpers;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leo on 2017-05-31.
 */
public class StringHelper {
    public static boolean isPhoneNumber(String phone) {
        Pattern p = Pattern.compile("^1[34578]\\d{9}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static boolean isEmail(CharSequence input) {
        Pattern p = Pattern.compile("^1[34578]\\d{9}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isNullOrWhitespace(CharSequence input) {
        return input == null || StringUtils.isEmpty(input) || StringUtils.isWhitespace(input);
    }

    public static boolean isNullOrEmpty(CharSequence input) {
        return input == null || StringUtils.isEmpty(input);
    }

}
