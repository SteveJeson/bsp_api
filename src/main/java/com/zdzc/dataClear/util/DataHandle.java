package com.zdzc.dataClear.util;

import com.zdzc.dataClear.entity.DataConst;
import com.zdzc.dataClear.entity.MonthDay;
import com.zdzc.dataClear.entity.SeqNoAttr;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *  * @Description: 获取指定日期前3个月零若干天的日期
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 15:00
 *  
 */
public class DataHandle {

    private static final Logger log = Logger.getLogger(DataHandle.class);

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
            if (type.equals(DataConst.TRAINCOLNAME)){
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

    /**备份数据库
     * 注意：该备份命令参数[--set-gtid-purged=OFF]默认服务器使用的mysql5.6+版本，因为gtid是mysql5.6+版本才引入的新特性，
     * mysql5.5版本暂未引入该特性，加上会报错，mysql5.6+版本不引入该参数还原数据库备份脚本执行不成功。
     mysqldump -h 127.0.0.1 -uroot -p123456 gps_main > .\gps_main.sql --set-gtid-purged=OFF
     * @Description:数据库备份
     * @Author chengwengao
     * @Date 2017/12/7 0007 14:33
     * @param hostName 数据库主机名
     * @param username 数据库用户名
     * @param passwd 数据库密码
     * @param sourceDbName 原将要备份的数据库名
     * @param destinatonPath 备份文件存放路径
     */
    public static String dbBackup(String hostName, String username, String passwd, String sourceDbName, String destinatonPath) {
        try {
            //获取数据库版本号
            //mysql  Ver 14.14 Distrib 5.5.28, for Win64 (x86)
            //mysql  Ver 14.14 Distrib 5.6.38, for Win64 (x86_64)
            String backShell = "mysqldump -h " + hostName + " -u" + username + " -p"+ passwd +" " + sourceDbName;    //备份脚本命令
            String dbVersion = exeCmd("mysql -V");
            if(dbVersion.indexOf("5.6") > -1){
                backShell += " --set-gtid-purged=OFF ";
            }
            Runtime rt = Runtime.getRuntime();

            // 调用 调用mysql的安装目录的命令
            Process child = rt.exec(backShell);
            // 设置导出编码为utf-8。这里必须是utf-8
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
            InputStream in = child.getInputStream();// 控制台的输出信息作为输入流

            InputStreamReader xx = new InputStreamReader(in, "utf-8");
            // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码

            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            // 组合控制台输出信息字符串
            BufferedReader br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();

            // 要用来做导入用的sql目标文件：
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(new Date());
            if (!new File(destinatonPath).exists()){
                new File(destinatonPath).mkdirs();
            }
            FileOutputStream fout = new FileOutputStream(destinatonPath + "\\" + sourceDbName + time);
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf-8");
            writer.write(outStr);
            writer.flush();
            in.close();
            xx.close();
            br.close();
            writer.close();
            fout.close();

            sb.delete(0, sb.length());
            sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n数据库版本：" + dbVersion + "\n备份命令：" + backShell + "\n数据库备份成功，文件名：" + destinatonPath + "\\" + sourceDbName + time);
            log.info(sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e.getCause());
            return "数据库备份失败，失败原因：\n" + e.getMessage();
        }
    }

    /**恢复数据库：
     (前提：该数据库已创建，CREATE DATABASE gps_main1)
     mysql -h 127.0.0.1 -uroot -p123456 gps_main1 < gps_main1.sql
     * @Description:数据库还原
     * @Author chengwengao
     * @Date 2017/12/7 0007 14:39
     * @param hostName 数据库主机名
     * @param username 数据库用户名
     * @param passwd 数据库密码
     * @param databaseName 数据库名
     * @param multipartFile 上传的数据库文件
     */
    public static String dbRevert(String hostName, String username, String passwd, String databaseName, MultipartFile multipartFile){
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime
                    .exec("mysql -h " + hostName + " -u" + username + " -p" + passwd + " --default-character-set=utf8 "
                            + databaseName);
            OutputStream outputStream = process.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), "utf-8"));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str + "\r\n");
            }
            str = sb.toString();
            // System.out.println(str);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream,
                    "utf-8");
            writer.write(str);
            writer.flush();
            outputStream.close();
            br.close();
            writer.close();

            sb.delete(0, sb.length());
            sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "数据库恢复成功，源文件：" + multipartFile.getOriginalFilename());
            log.info(sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e.getCause());
            return "数据库还原失败，失败原因：\n" + e.getMessage();
        }
    }

    /**
     * @Description:执行cmd命令，获取执行结果
     * @Author chengwengao
     * @Date 2017/12/22 0022 12:33
     */
    public static String exeCmd(String commandStr) throws Exception{
        BufferedReader br = null;
        Process p = Runtime.getRuntime().exec(commandStr);
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }
}
