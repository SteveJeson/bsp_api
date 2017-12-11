package com.zdzc.dataClear.entity;

import java.util.HashMap;
import java.util.Map;

/**
 *  * @Description: 常量工具类，字符常量
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 14:48
 *  
 */
public class DataConst {

    public static final int TRAILCOUNTPERDB = 100000;   //轨迹信息，单个数据库存放的车辆数量，10w
    public static final int ALARMCOUNTPERDB = 3000000;   //报警信息，单个数据库存放的车辆数量，300w
    public static final String TRAILDBPREFIX = "gps_"; //轨迹数据库前缀
    public static final String ALARMDBPREFIX = "gps_alarm_"; //报警数据库前缀
    public static final String TRAILTABPREFIX = "t_gps_";  //轨迹表前缀
    public static final  String ALARMTABPREFIX = "t_gps_alarm_";  //报警表前缀
    public static final int DIVMON = 3;   //指定向前推的若干个月
    public static final int DIVDAY = 1;   //指定向前推的若干天
    public static final String TRAINCOLNAME = "trail_seq_no"; //轨迹序列号列名
    public static final String ALARMCOLNAME = "alarm_seq_no"; //报警序列号列名
    public static final String TRAILCREATEOPER = "创建轨迹表";
    public static final String TRAILDROPOPER = "删除轨迹表";
    public static final String ALARMCREATEOPER = "创建报警表";
    public static final String ALARMDROPOPER = "删除报警表";

    public static final int MAXTRAIN = 90000;   //轨迹信息，单个数据库存放的最大触发报警数，9w
    public static final int MAXALARM = 2900000;   //报警信息，单个数据库存放的最大触发报警数，290w

    public static final int PUSH_INTERVAL = 1000*60*60*12;   //推送时间间隔，单位毫秒
//    public static final int PUSH_INTERVAL = 3000;   //推送时间间隔，单位毫秒

    /**
     * 月份与分布表[a, b, c, d]对应关系map
     * k:月份 v:分布表
     */
    public static Map MONTH_MUL_TAB_MAP = new HashMap(){
        {
            put(1,"a");
            put(2,"b");
            put(3,"c");
            put(4,"d");
            put(5,"a");
            put(6,"b");
            put(7,"c");
            put(8,"d");
            put(9,"a");
            put(10,"b");
            put(11,"c");
            put(12,"a");
        }
    };
}
