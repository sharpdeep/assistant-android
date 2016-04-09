package com.sharpdeep.assistant_android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bear on 16-4-9.
 */
public class DateUtil {

    public static String getWeekStrByDateStr(String dateStr,String dateFormat){
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(dateStr));
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
}
