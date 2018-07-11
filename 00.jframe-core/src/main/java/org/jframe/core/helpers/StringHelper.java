package org.jframe.core.helpers;

import org.apache.commons.lang3.StringUtils;
import org.jframe.core.extensions.JDate;
import org.jframe.core.exception.KnownException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leo on 2017-05-31.
 */
public class StringHelper {

    private static final char[] imageCaptchaChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static final char[] phoneCaptchaChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static Pattern patternPhone = Pattern.compile("^1[34578]\\d{9}$");
    private static Pattern patternEmail = Pattern.compile("^\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}$");

    private static BigDecimal PERCENT_RANGE = new BigDecimal("999");

    /**
     * 判断两个字符串是否相等，不区分大小写，如果都为null则也会返回相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean eq(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static boolean isPhoneNumber(String phone) {
        if (StringHelper.isNullOrWhitespace(phone)) {
            return false;
        }
        Matcher m = patternPhone.matcher(phone);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        if (StringHelper.isNullOrWhitespace(email)) {
            return false;
        }
        Matcher m = patternEmail.matcher(email);
        return m.matches();
    }

    public static boolean isNullOrWhitespace(CharSequence input) {
        return input == null || StringUtils.isEmpty(input) || StringUtils.isWhitespace(input);
    }

    public static boolean isNullOrEmpty(CharSequence input) {
        return input == null || StringUtils.isEmpty(input);
    }

    public static String newImageCaptchaCode() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            sb.append(String.valueOf(imageCaptchaChars[random.nextInt(imageCaptchaChars.length)]));
        }
        return sb.toString();
    }

    public static String newPhoneCaptchaCode() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(String.valueOf(phoneCaptchaChars[random.nextInt(phoneCaptchaChars.length)]));
        }
        return sb.toString();
    }

    public static String newUuid() {
        return UUID.randomUUID().toString();
    }

    public static String newUuidNoDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void validate_notNullOrWhitespace(String value, String errorMessage) {
        if (StringHelper.isNullOrWhitespace(value)) {
            throw new KnownException(errorMessage);
        }
    }

    public static void validate_phoneNumber(String value, String errorMessage) {
        if (StringHelper.isPhoneNumber(value) == false) {
            throw new KnownException(errorMessage);
        }
    }

    public static String toFixedLength(Integer value, int length) {
        StringBuilder sb = new StringBuilder(value.toString());
        while (sb.length() < length) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    /**
     * 产生给定位数的随机数字符串
     *
     * @param length
     * @return
     */
    public static String randomNumber(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(String.valueOf(phoneCaptchaChars[random.nextInt(phoneCaptchaChars.length)]));
        }
        return sb.toString();
    }

    public static String hidePhoneNumber(String input) {
        if (!StringHelper.isPhoneNumber(input)) {
            return input;
        }
        return input.substring(0, 3) + "****" + input.substring(7);
    }

    public static String hideNickName(String nickName) {
        if (nickName.length() > 2) {
            return nickName.substring(0, 1) + "***" + nickName.substring(nickName.length() - 1, nickName.length());
        }
        return nickName;
    }

    public static boolean isEmoji(char ch) {
        return !(ch == 0x0 || ch == 0x9 || ch == 0xA || ch == 0xD || ch >= 0x20 && ch <= 0xD7FF || ch >= 0xE000 && ch <= 0xFFFD);
    }

    /**
     * 清除一个字符串中的emoji表情字符
     */
    public static String cleanEmoji(String input) {
        if (isNullOrWhitespace(input)) {
            return null;
        }
        StringBuilder builder = new StringBuilder(input);
        for (int i = 0; i < builder.length(); i++) {
            if (isEmoji(builder.charAt(i))) {
                builder.deleteCharAt(i);
                builder.insert(i, ' ');
            }
        }
        return builder.toString().trim();
    }

    /**
     * yyyyMMdd
     * StringDate yyyy-MM-dd
     */
    public static String covertDateToString(Integer date) {
        String oldDate = String.valueOf(date);
        String year = oldDate.substring(0, 4);
        String month = oldDate.substring(4, 6);
        String day = oldDate.substring(6);
        return year + "-" + month + "-" + day;
    }

    /**
     * StringDate yyyy-MM-dd 等日期格式
     * IntegerDate yyyyMMdd
     */
    public static Integer covertDateToInteger(String date) {
        return Integer.valueOf(JDate.parseFrom(date).toString("yyyyMMdd"));
    }

    /**
     * return String xx.xx%
     */
    public static String convertPercent(BigDecimal decimal, int scale) {
        if (DecimalHelper.isZero(decimal)) {
            return "-";
        }

        BigDecimal percentResult = decimal.abs().multiply(new BigDecimal(100)).setScale(scale, BigDecimal.ROUND_HALF_UP);

        if (percentResult.compareTo(PERCENT_RANGE) > 0) {
            return ">" + PERCENT_RANGE + "%";
        }

        return percentResult.toString() + "%";
    }

    /**
     * return String ￥xx.xx
     */
    public static String convertAmount(BigDecimal decimal, boolean isMoneySign) {
        DecimalFormat percentFormat;
        if (isMoneySign) {
            percentFormat = new DecimalFormat("￥,##0.00");
        } else {
            percentFormat = new DecimalFormat("0.00");
        }
        return percentFormat.format(decimal);
    }

    public static String convertSeconds(int second) {
        if (second < 60) {
            return second + "秒";
        }
        if (second < 60 * 60) {
            return second / 60 + "分" + (second % 60 == 0 ? "" : second % 60 + "秒");
        }
        int hours = second / (60 * 60);
        int minutes = (second % (60 * 60)) / 60;
        int seconds = second % 60;
        return hours + "小时" + (minutes == 0 ? "" : minutes + "分") + (seconds == 0 ? "" : seconds + "秒");
    }
}
