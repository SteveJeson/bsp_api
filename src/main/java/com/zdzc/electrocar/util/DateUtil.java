package com.zdzc.electrocar.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    /**
     * 根据传入的时间获取获取年份，传入参数为null，则返回当前系统时间的年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        if (date == null){
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 根据传入的时间获取获取月份，传入参数为null，则返回当前系统时间的月份
     * @param date
     * @return
     */
    public static int getMonth(Date date){
        if (date == null){
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH ) + 1;
    }

    /**
     * 根据传入的时间获取获取日期，传入参数为null，则返回当前系统时间的日期
     * @param date
     * @return
     */
    public static int getDay(Date date){
        if (date == null){
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
