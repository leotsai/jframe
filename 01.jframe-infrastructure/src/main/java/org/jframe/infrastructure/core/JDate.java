package org.jframe.infrastructure.core;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by leo on 2017-06-29.
 */
public class JDate extends Date {

    public JDate(Date date){
        super(date.getTime());
    }

    public JDate(long time){
        super(time);
    }

    public JDate getDatePart(){
        return new JDate(DateUtils.truncate(this, Calendar.DATE));
    }

    public String toString(String pattern){
        return DateFormatUtils.format(this, pattern);
    }

    public String toDateString(){
        return DateFormatUtils.format(this, "yyyy-MM-dd");
    }

    public JDate addSeconds(int seconds){
        this.setSeconds(this.getSeconds() + seconds);
        return this;
    }

    public JDate addMinuts(int minuts) {
        this.setMinutes(this.getMinutes() + minuts);
        return this;
    }

    public JDate addHours(int hours){
        this.setHours(this.getHours() + hours);
        return this;
    }

    public JDate addDays(int days){
        this.setDate(this.getDate() + days);
        return this;
    }

    public JDate addMonths(int months){
        this.setMonth(this.getMonth() + months);
        return this;
    }

    public JDate addYears(int years){
        this.setYear(this.getYear() + years);
        return this;
    }


    public JDate toMonday() {
        if (this.getDay() == 0) {
            return this.addDays(-6);
        }
        return this.addDays(1 - this.getDay());
    }

    public JDate toSunday() {
        if (this.getDay() == 0) {
            return this;
        }
        return this.addDays(7 - this.getDay());
    }

    public JDate toMonthFirstDay(){
        this.setDate(1);
        return this;
    }

    public float countDays(JDate pastDate){
        return ((this.getTime() - pastDate.getTime())/86400000f);
    }


    public String toTimeString(){
        return DateFormatUtils.format(this, "HH:mm:ss");
    }

    public static JDate parseFrom(String date) throws Exception{
        return new JDate(DateUtils.parseDate(date, new String[]{
                "yyyy-MM-dd", "yyyy/MM/dd",
                "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"
        }));
    }

    public static JDate parseFrom(String date, String... pattern) throws Exception{
        return new JDate(DateUtils.parseDate(date, pattern));
    }

    public static JDate from(Date date){
        return  new JDate(date);
    }


}
