package com.sharpdeep.assistant_android.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by bear on 16-4-9.
 */
public class DateUtil {

    public static String getWeekStrByDateStr(String dateStr,String dateFormat){
        SimpleDateFormat formater = new SimpleDateFormat(dateFormat);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formater.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
        }
        return "";
    }

    public static String getDateStr(int year, int month, int day_of_month,String dateFormat){
        Calendar c = Calendar.getInstance();
        c.set(year,month-1,day_of_month);
        return getDateStr(c.getTime(),dateFormat);
    }

    public static String getDateStr(Date date, String dateFormat){
        SimpleDateFormat formater = new SimpleDateFormat(dateFormat);
        return formater.format(date);
    }
}
