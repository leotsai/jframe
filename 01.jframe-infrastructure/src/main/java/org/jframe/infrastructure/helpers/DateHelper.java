package org.jframe.infrastructure.helpers;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by leo on 2017-09-04.
 */
public class DateHelper {

    /**
     * 一天的毫秒数
     */
    private static final Long MILLISECOND_OF_DAY = 24 * 60 * 60 * 1000L;

    /**
     * 一秒钟的毫秒数
     */
    private static final Long MILLISECOND_OF_SECOND = 1000L;

    public static String CHINESE_TIME_NO_SEC = "yyyy年MM月dd日 HH:mm";

    public static String toTime(Date date) {
        if (null == date) {
            return "";
        }
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toTimeSlash(Date date) {
        if (null == date) {
            return "";
        }
        return DateFormatUtils.format(date, "yyyy/MM/dd HH:mm:ss");
    }

    public static String toMonthTime(Date date) {
        if (null == date) {
            return "";
        }
        return DateFormatUtils.format(date, "MM-dd HH:mm");
    }

    public static String toCompleteTime(Date date) {
        if (null == date) {
            return "";
        }
        return DateFormatUtils.format(date, "yyyyMMddHHmmss");
    }

    public static String toDate(Date date) {
        if (null == date) {
            return "";
        }
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }

    public static String logChineseDate(Date date) {
        if (null == date) {
            return "";
        }
        if (date.before(yearFirstDate(new Date()))) {
            return DateFormatUtils.format(date, "yy年MM月");
        } else if (date.before(monthFirstDate(new Date()))) {
            return DateFormatUtils.format(date, "MM月");
        } else {
            return "本月";
        }
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return DateFormatUtils.format(date, pattern);
    }

    public static String getExpireTimeText(Date fromTime, Date toTime) {
        String pattern = "yyyy/MM/dd HH:mm:ss";
        if (fromTime == null && toTime == null) {
            return "长期有效";
        }
        if ((fromTime != null && toTime != null)) {
            return format(fromTime, pattern) + " 至 " + format(toTime, pattern);
        }
        if (fromTime == null) {
            return "立即生效" + " 至 " + format(toTime, pattern);
        }
        return format(fromTime, pattern) + " 至 永久";
    }

    public static String getSpaceExpireTime(Date fromTime, Date toTime) {
        String pattern = "yyyy/MM/dd HH:mm:ss";
        if (fromTime == null && toTime == null) {
            return "长期有效";
        }
        if ((fromTime != null && toTime != null)) {
            return format(fromTime, pattern) + "<p>至</p>" + format(toTime, pattern);
        }
        if (fromTime == null) {
            return "立即生效" + " 至 " + format(toTime, pattern);
        }
        return format(fromTime, pattern) + " <p>至</p> 永久";
    }

    public static String getExpireDateText(Date fromDate, Date toDate) {
        String pattern = "yyyy/MM/dd";
        if (fromDate == null && toDate == null) {
            return "长期有效";
        }
        if ((fromDate != null && toDate != null)) {
            return format(fromDate, pattern) + " 至 " + format(toDate, pattern);
        }
        if (fromDate == null) {
            return "立即生效" + " 至 " + format(toDate, pattern);
        }
        return format(fromDate, pattern) + " 至 永久";
    }

    public static String toChineseDate(Date date) {
        if (date == null) {
            return "";
        }
        return DateFormatUtils.format(date, "yyy年MM月dd日");
    }

    public static String toChineseTime(Date date) {
        if (date == null) {
            return "";
        }
        return DateFormatUtils.format(date, "yyy年MM月dd日 HH时mm分ss秒");
    }

    /**
     * 返回下一天，如果date为null则默认返回明天
     *
     * @param date
     * @return
     */
    public static String getNextDateString(Date date) {
        if (null == date) {
            return null;
        }
        return toDate(nextDate(date));
    }

    public static Date timeToDate(Date time) {
        if (null == time) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 返回上一天，如果date为null则默认返回明天
     *
     * @param date
     * @return
     */
    public static String getPrevDateString(Date date) {
        if (null == date) {
            return null;
        }
        return toDate(prevDate(date));
    }

    /**
     * 返回下一天，如果date为null则默认返回明天
     *
     * @param date
     * @return
     */
    public static Date nextDate(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 返回上一天，如果date为null则默认返回明天
     *
     * @param date
     * @return
     */
    public static Date prevDate(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 比较时间
     *
     * @param date1
     * @param date2
     * @return 1：date1>date2;-1：date1<date2;0：date1=date2
     */
    public static int compare(Date date1, Date date2) {
        if (Objects.equals(date1, date2) && date1 == null) {
            return 0;
        } else if (date1 == null && date2 != null) {
            return -1;
        } else if (date1 != null && date2 == null) {
            return 1;
        }

        if (date1.getTime() > date2.getTime()) {
            return 1;
        } else if (date1.getTime() < date2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 计算两个日期相差的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int calculateDays(Date start, Date end) {
        long milliseconds = end.getTime() - start.getTime();
        int days = (int) (milliseconds / MILLISECOND_OF_DAY);
        return days;
    }

    /**
     * 计算两个日期相差的秒数
     *
     * @param start
     * @param end
     * @return
     */
    public static long calculateRemainingSeconds(Date start, Date end) {
        long milliseconds = end.getTime() - start.getTime();
        long seconds = (milliseconds / MILLISECOND_OF_SECOND);
        return seconds;
    }

    /**
     * 天数加减
     *
     * @param date
     * @param day
     * @param isBegin 是否 00:00:00
     * @return
     */
    public static Date dayCalculate(Date date, int day, boolean isBegin) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.add(Calendar.DATE, day);
        if (isBegin) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date yearFirstDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date monthFirstDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static int getBirthYearByAge(Integer age) {
        if (age == null || age <= 0) {
            return 0;
        }
        return Calendar.getInstance().get(Calendar.YEAR) - age;
    }

    public static int getAgeByBirthYear(Integer birthYear) {
        if (birthYear == null || birthYear <= 0) {
            return 0;
        }
        return Calendar.getInstance().get(Calendar.YEAR) - birthYear;
    }
}
