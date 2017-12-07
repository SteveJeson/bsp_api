package com.zdzc.dataClear.util;

import com.zdzc.dataClear.entity.DataConst;
import com.zdzc.dataClear.entity.MonthDay;
import com.zdzc.dataClear.entity.SeqNoAttr;

import java.util.Calendar;

/**
 *  * @Description: 获取指定日期前3个月零若干天的日期
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 15:00
 *  
 */
public class DataHandle {
    private static final String TRAINCOLNAME = "trail_seq_no"; //轨迹序列号列名
    /**
     * 往前推3个月，减去指定天数（3,1）
     当前时间：2018年5月30
     换算时间：2018年2月27
     * @param divMon 往前推的月份
     * @param divDay 往前推的天数
     */
    public static MonthDay divideTime(int divMon, int divDay){
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
     * @Description:获取要删除的表名前缀,如't_gps_a1_[1-10]',注意，此处的下划线不是正则匹配的，需要转义
     gps_1.t_gps_a3_1
     gps_1.t_gps_a3_2
     gps_1.t_gps_a3_3
     gps_1.t_gps_a3_4
     gps_1.t_gps_a3_5
     gps_1.t_gps_a3_6
     gps_1.t_gps_a3_7
     gps_1.t_gps_a3_8
     gps_1.t_gps_a3_9
     gps_1.t_gps_a3_10
     当前时间：2017年12月4日
     换算时间：2017年9月3日
     * @Author chengwengao
     * @Date 2017/12/1 0001 15:12
     * @param type 类型
     * @param tablePrefix 表名前缀
     * @param divMon 往前推的月份
     * @param divDay 往前推的天数
     */
    public static String getDelTabPrefix(String type, String tablePrefix, int divMon, int divDay){
        String tabPrefix = null;
        MonthDay monthDay = divideTime(divMon, divDay);
        if(DataConst.MONTH_MUL_TAB_MAP.containsKey(monthDay.getMon())){
            if (type.equals(TRAINCOLNAME)){
                //报警表每天一张表，诸如：t_gps_a3_10
                tabPrefix = tablePrefix + DataConst.MONTH_MUL_TAB_MAP.get(monthDay.getMon()) + String.valueOf(monthDay.getDay()) + "\\_";
            }else{
                //报警表每月150张表，诸如：t_gps_alarm_a_1
                tabPrefix = tablePrefix + DataConst.MONTH_MUL_TAB_MAP.get(monthDay.getMon()) + "\\_";
            }
        }

        return tabPrefix;
    }

    /**
      * @Description:通过报警序列号得到报警数据库数量
      * @Author chengwengao
     * @Date 2017/12/5 0005 15:34
     * @param alarm_seq_no 报警序列号
     * @param subIndex 截取位数
     */
    public static SeqNoAttr getAlarmDBCount(Long alarm_seq_no, int subIndex){
        SeqNoAttr seqNoAttr = new SeqNoAttr();
        if(alarm_seq_no == 0L){
            seqNoAttr.setHead(0L);
            seqNoAttr.setTail(0L);
            seqNoAttr.setSeqNo(0L);
        }else if(String.valueOf(alarm_seq_no).length() <= subIndex){
            seqNoAttr.setMsg("数据格式不符合分库策略，请检查数据源！");
        }else{
            String s = new Reverse(String.valueOf(alarm_seq_no)).reverseByBit();   //反转后字串
            seqNoAttr.setHead(Long.valueOf(new Reverse(s.substring(subIndex, s.length())).reverseByBit())); //库序号
            seqNoAttr.setTail(Long.valueOf(new Reverse(s.substring(0, subIndex)).reverseByBit()));
            seqNoAttr.setSeqNo(alarm_seq_no);   //完整序列号
        }
        return seqNoAttr;
    }

    /**
     * 判断字符串是否为空
     * @param arr
     * @return
     */
    public static boolean isEmpty(String arr){
        if(!(null == arr || arr.length()==0)){
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否非空
     * @param arr
     * @return
     */
    public static boolean isNotEmpty(String arr){
        return !isEmpty(arr);
    }
}
