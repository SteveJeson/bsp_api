package com.zdzc.electrocar.util;

import com.zdzc.electrocar.common.Const;
import org.springframework.util.StringUtils;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
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

    public static boolean isSameDay(String startTime, String endTime){
//        if (startTime != null && endTime != null){
//            return DateUtil.getYear(startTime) == DateUtil.getYear(endTime) &&
//                    DateUtil.getMonth(startTime) == DateUtil.getMonth(endTime) &&
//                    DateUtil.getDay(startTime) == DateUtil.getDay(endTime);
//        }
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && startTime.length() > 6 && endTime.length() > 6){
            return startTime.substring(0,2).equals(endTime.substring(0,2)) && startTime.substring(2,4).equals(endTime.substring(2,4))
                    && startTime.substring(4,6).equals(endTime.substring(4,6));
        }
        return false;
    }


    public static boolean isSameMonth(String startTime, String endTime){
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && startTime.length() > 6 && endTime.length() > 6){
            return startTime.substring(0,2).equals(endTime.substring(0,2)) && startTime.substring(2,4).equals(endTime.substring(2,4));
        }
        return false;
    }

    /**
     * 把 YYyy-MM-dd HH:mm:ss 的时间格式转化成 yyMMddHHmmss
     * @param date
     * @return
     */
    public static String transformDateTimeStr(String date){
        StringBuilder dateStr = new StringBuilder();
        if (!StringUtils.isEmpty(date)){
            String[] ymd = date.split(" ")[0].split("-");
            String[] hms = date.split(" ")[1].split(":");
            return dateStr.append(ymd[0]).append(ymd[1]).append(ymd[2]).append(hms[0]).append(hms[1]).append(hms[2]).toString().substring(2);
        }
        return dateStr.toString();
    }

    /**
     * 把 yyMMddHHmmss 格式的字符串转化成 date 实体
     * @param dateTimeStr
     * @return
     */
    public static Date transDateTimeStrToDate(String dateTimeStr) throws Exception{
        if (!StringUtils.isEmpty(dateTimeStr) && dateTimeStr.length() >= 12) {
            SimpleDateFormat sdf = new SimpleDateFormat(Const.Date.Y_M_D_HMS);
            StringBuilder sb = new StringBuilder();
            String date = sb.append("20").append(dateTimeStr.substring(0, 2)).append("-").append(dateTimeStr.substring(2, 4))
                    .append("-").append(dateTimeStr.substring(4, 6)).append(" ").append(dateTimeStr.substring(6, 8)).append(":")
                    .append(dateTimeStr.substring(8, 10)).append(":").append(dateTimeStr.substring(10, 12)).toString();
            return sdf.parse(date);
        }

        return new Date();
    }


}
