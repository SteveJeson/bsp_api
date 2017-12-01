package com.zdzc.dataClear.util;

import com.zdzc.dataClear.entity.DataConst;
import com.zdzc.dataClear.entity.MonthDay;

import java.util.Calendar;

/**
 *  * @Description: 获取指定日期前3个月零若干天的日期
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 15:00
 *  
 */
public class DataHandle {

    /**
     * 往前推3个月，减去指定天数（3,1）
     当前时间：2018年5月30
     换算时间：2018年2月27
     * @param divMon 往前推的月份
     * @param divDay 往前推的天数
     */
    public MonthDay divideTime(int divMon, int divDay){
        Calendar cal = Calendar.getInstance();
        System.out.println("当前时间：" + cal.get(Calendar.YEAR)+ "年" + (cal.get(Calendar.MONTH)+1) + "月" + cal.get(Calendar.DATE) + "日");
        cal.set(Calendar.YEAR,cal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        cal.set(Calendar.DATE, cal.get(Calendar.DATE));
        cal.add(Calendar.MONTH, -divMon);
        cal.add(Calendar.DATE, -divDay);
        System.out.println("换算时间：" + cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH) + 1)+"月"+cal.get(Calendar.DATE) + "日");
        return new MonthDay(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
    }

    /**
     * @Description:获取要删除的表名前缀
     * @Author chengwengao
     * @Date 2017/12/1 0001 15:12
     */
    public String getDelTabPrefix(int divMon, int divDay){
        String tabPrefix = null;
        MonthDay monthDay = divideTime(divMon, divDay);
        int currentMon = Calendar.getInstance().get(Calendar.MONTH) + 1;    //当前月份数
        if(DataConst.MONTH_MUL_TAB_MAP.containsKey(currentMon)){
            tabPrefix = DataConst.MONTH_MUL_TAB_MAP.get(currentMon) + String.valueOf(monthDay.getMon()) + "_";
        }

        return tabPrefix;
    }
}
