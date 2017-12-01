package com.zdzc.electrocar.common;

import com.zdzc.electrocar.util.JSONResult;
import com.zdzc.electrocar.util.StatusCode;
import com.zdzc.electrocar.util.threadPool.DefaultThreadPool;

/**
 * Created by Administrator on 2017/9/12 0012.
 */
public interface Const {

    class DateBase {
        public static final String TRAIL_DB_PREFIX = "gps_";
        public static final String TRAIL_TABLE_PREFIX = "t_gps_";
        public static final String ALARM_DB_PREFIX = "gps_alarm_";
        public static final String ALARM_TABLE_PREFIX = "t_gps_alarm_";
        public static final String TABLE_NAME = "tableName";
        public static final String DB_NAME = "dbName";
    }

    class Date{
        public static final String Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";
    }

    class Public{
        public static final JSONResult JSON_RESULT = new JSONResult();
        public static final DefaultThreadPool THREAD_POOL = new DefaultThreadPool(20);
        public static final String SUCCESS = "请求成功！";
        public static final String NOT_IN_THE_SAME_DAY = "查询时间请保持在同一天内!";
        public static final String NOT_IN_THE_SAME_MONTH = "查询时间请保持在同一月内！";
        public static final String NO_RESULT = "未查询到符合条件的数据！";
        public static final String[] MONTH_INDEX = {"a", "b", "c", "d"};
        public static final String TOKEN_ERROR = "TOKEN 验证失败！";
        public static final String SYSTEM_ERROR = "系统异常！";
        public static final String PAGE_NO = "pageNo";
        public static final String PAGE_SIZE = "pageSize";
    }

    class Fields{
        public static final String DEVICE_CODE = "deviceCode";
        public static final String BEGIN_TIME = "beginTime";
        public static final String END_TIME = "endTime";
        public static final String ALARM_HANDLE = "alarmHandle";
        public static final String PARK_TIME = "parkTime";
        public static final String TRAILS = "trails";
        public static final String PARKER_POINTS = "parkerPoints";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String POSITON = "position";
    }

    class VehicleStatus{
        public static final Integer OK = 440002;
        public static final Integer INVALID_POSITON = 440000;
    }
}
