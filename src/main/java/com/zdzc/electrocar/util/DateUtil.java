package com.zdzc.electrocar.util;

import com.zdzc.electrocar.common.Const;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final Calendar calendar = Calendar.getInstance();
    /**
     * 根据传入的时间获取获取年份，传入参数为null，则返回当前系统时间的年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        if (date == null){
            date = new Date();
        }
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
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameDay(Date startTime, Date endTime){
        if (startTime != null && endTime != null){
            return DateUtil.getYear(startTime) == DateUtil.getYear(endTime) &&
                    DateUtil.getMonth(startTime) == DateUtil.getMonth(endTime) &&
                    DateUtil.getDay(startTime) == DateUtil.getDay(endTime);
        }
        return false;
    }


    public static boolean isSameMonth(Date startTime, Date endTime){
        if (startTime != null && endTime != null){
            return DateUtil.getYear(startTime) == DateUtil.getYear(endTime) &&
                    DateUtil.getMonth(startTime) == DateUtil.getMonth(endTime);
        }
        return false;
    }

    /**
     *  把毫秒数换算成时分秒
     * @param time  毫秒数
     * @return
     */
    public static String calculateTime(long time){
        int parkerTime = (int)time / 1000;
        int hour = parkerTime / 3600;
        int hourMod = parkerTime % 3600;
        int minute = hourMod / 60;
        int minuteMod = hourMod % 60;
        StringBuilder parkerTimeStr = new StringBuilder();
        return parkerTimeStr.append(hour).append("小时").append(minute).append("分钟").append(minuteMod).append("秒").toString();
    }
}
