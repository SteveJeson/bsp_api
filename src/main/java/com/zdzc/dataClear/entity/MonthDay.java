package com.zdzc.dataClear.entity;

/**
 *  * @Description: divideTime()推算得出的月份和日期
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 15:05
 *  
 */
public class MonthDay {
    public int mon; //月份
    public int day; //天数

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public MonthDay(int mon, int day) {
        this.mon = mon;
        this.day = day;
    }
}
