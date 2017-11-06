package com.zdzc.electrocar.util;

import java.math.BigDecimal;

public class StringUtil {

    /**
     *比较两个百分比大小
     * @param str1  被比数字符串
     * @param str2  比数字符串
     * @return result 1：大于 0：相同 -1：小于
     */
    public static int compare(String str1, String str2) {

        //去掉%
        String tempA = str1.replace("%","");
        String tempB = str2.replace("%","");
        //精确表示
        BigDecimal dataA = new BigDecimal(tempA);
        BigDecimal dataB = new BigDecimal(tempB);
        int result = dataA.compareTo(dataB);
        return result;
    }

    /**
     * 判断某个百分比数是否在某两个百分比范围之内
     * @param source 要比较的目标百分比字符串
     * @param begin 开始百分比字符串
     * @param end   结束百分比字符串
     * @return boolean true表示在范围之内  false表示不在范围之内
     */
    public static boolean isInDecScope(String source, String begin, String end) {
        int b = StringUtil.compare(source, begin);
        int e = StringUtil.compare(source,end);
        if (b >= 0 && e <=0) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个字符串是否为空
     * @param source 目标字符串
     * @return
     */
    public static boolean isEmpty(String source){
        return source == null || "".equals(source);
    }



}
