package com.zdzc.dataClear.entity;

import java.util.HashMap;
import java.util.Map;

/**
 *  * @Description: 常量工具类
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 14:48
 *  
 */
public class DataConst {

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
