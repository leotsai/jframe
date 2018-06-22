package org.jframe.core.extensions;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by leo on 2017-06-29.
 */
public class JDate extends Date {

    public JDate() {
        super(System.currentTimeMillis());
    }

    public JDate(Date date) {
        super(date.getTime());
    }

    public JDate(long time) {
        super(time);
    }

    public JDate getDatePart() {
        return new JDate(DateUtils.truncate(this, Calendar.DATE));
    }

    public JDate addSeconds(int seconds) {
        return new JDate(DateUtils.addSeconds(this, seconds));
    }

    public JDate addMinuts(int minuts) {
        return new JDate(DateUtils.addMinutes(this, minuts));
    }

    public JDate addHours(int hours) {
        return new JDate(DateUtils.addHours(this, hours));
    }

    public JDate addDays(int days) {
        return new JDate(DateUtils.addDays(this, days));
    }

    public JDate addMonths(int months) {
        return new JDate(DateUtils.addMonths(this, months));
    }

    public JDate addYears(int years) {
        return new JDate(DateUtils.addYears(this, years));
    }

    public DayOfWeek getDayOfWeek() {
        int dayOfWeek = DateUtils.toCalendar(this).get(Calendar.DAY_OF_WEEK);
        return DayOfWeek.of(dayOfWeek == 1 ? 7 : dayOfWeek - 1);
    }

    public Long getVersionMiliSeconds() {
        return this.getTime() - JDate.parseFrom("1970/1/1 08:00:00").getTime();
    }

    public Long getVersionSeconds() {
        return this.getVersionMiliSeconds() / 1000;
    }

    public JDate toMonday() {
        DayOfWeek dayOfWeek = this.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return this.addDays(-6);
        }
        return this.addDays(1 - dayOfWeek.getValue());
    }

    public JDate toSunday() {
        DayOfWeek dayOfWeek = this.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return this;
        }
        return this.addDays(7 - dayOfWeek.getValue());
    }

    public JDate toMonthFirstDay() {
        Calendar calendar = DateUtils.toCalendar(this);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return new JDate(calendar.getTime());
    }

    public boolean isGreaterThan(Date anotherDate) {
        return this.getTime() > anotherDate.getTime();
    }

    public boolean isGreaterOrEqual(Date anotherDate) {
        return this.getTime() >= anotherDate.getTime();
    }

    public boolean isLessThan(Date anotherDate) {
        return this.getTime() < anotherDate.getTime();
    }

    public boolean isLessOrEqual(Date anotherDate) {
        return this.getTime() <= anotherDate.getTime();
    }

    public float countDays(JDate pastDate) {
        return ((this.getTime() - pastDate.getTime()) / 86400000f);
    }

    public String toString(String pattern) {
        return DateFormatUtils.format(this, pattern);
    }

    public String toDateString() {
        return DateFormatUtils.format(this, "yyyy-MM-dd");
    }

    public String toTimeString() {
        return DateFormatUtils.format(this, "HH:mm:ss");
    }

    public String toDateTimeString() {
        return DateFormatUtils.format(this, "yyyy-MM-dd HH:mm:ss");
    }

    public String toChineseDateTimeString() {
        return DateFormatUtils.format(this, "yyyy年MM月dd日 HH:mm");
    }

    public static JDate parseFrom(String date) {
        try {
            return new JDate(DateUtils.parseDate(date, new String[]{
                    "yyyy-MM-dd",
                    "yyyy-MM-dd HH:mm",
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy/MM/dd",
                    "yyyy/MM/dd HH:mm",
                    "yyyy/MM/dd HH:mm:ss",
                    "yyyy.MM.dd",
                    "yyyy.MM.dd HH:mm",
                    "yyyy.MM.dd HH:mm:ss"
            }));
        } catch (Exception ex) {
            throw new KnownException("输入的字符串（" + date + "）不是一个正确的时间");
        }
    }

    public static JDate tryParseFrom(String date) {
        try {
            return parseFrom(date);
        } catch (Exception ex) {
            return null;
        }
    }

    public static JDate parseFrom(String date, String... pattern) throws Exception {
        return new JDate(DateUtils.parseDate(date, pattern));
    }

    public static JDate from(Date date) {
        return new JDate(date);
    }

    public static JDate today() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new JDate(calendar.getTime());
    }

    public static JDate yesterday() {
        return JDate.today().addDays(-1);
    }

    public static JDate tomorrow() {
        return JDate.today().addDays(1);
    }

    public static JDate now() {
        return new JDate();
    }


    public static JDate lastWeekStartDate(Date date) {
        JDate thisWeekStart = weekStartDate(date);
        JDate lastWeekStartDate = thisWeekStart.addDays(-7);
        return lastWeekStartDate;
    }

    public static JDate lastWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        return new JDate(calendar.getTime());
    }

    public static JDate weekStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new JDate(calendar.getTime());
    }

    /**
     * @param date
     * @return
     */
    public static JDate weekEndDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        int days = calendar.get(Calendar.DAY_OF_WEEK);
        if (days != 1) {
            calendar.add(Calendar.DAY_OF_MONTH, 7 - days + 1);
        }
        return new JDate(calendar.getTime());
    }

    public static JDate monthStartDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new JDate(calendar.getTime());
    }

    public static JDate monthLastDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return new JDate(calendar.getTime());
    }

    public static JDate monthFirstDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return new JDate(calendar.getTime());
    }
}
