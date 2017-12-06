package com.zdzc.dataClear.schedule;

import com.zdzc.dataClear.entity.Schema;
import com.zdzc.dataClear.service.DataClearService;
import com.zdzc.dataClear.util.DataHandle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  * @Description: 清表与创建定时任务
 *  * @author chengwengao
 *  * @date 2017/12/4 0004 13:18
 *  
 */
@Component
public class ClearTablesSchedule {

    private static final Logger log = Logger.getLogger(ClearTablesSchedule.class);

    @Autowired
    private DataClearService dataClearService;

    private final int TRAILCOUNTPERDB = 100000;   //轨迹信息，单个数据库存放的车辆数量，10w
    private final int ALARMCOUNTPERDB = 3000000;   //报警信息，单个数据库存放的车辆数量，300w
    private final String TRAILDBPREFIX = "gps_"; //轨迹数据库前缀
    private final String ALARMDBPREFIX = "gps_alarm_"; //报警数据库前缀
    private final static String TRAILTABPREFIX = "t_gps_";  //轨迹表前缀
    private final static String ALARMTABPREFIX = "t_gps_alarm_";  //报警表前缀
    private final int DIVMON = 3;   //指定向前推的若干个月
    private final int DIVDAY = 1;   //指定向前推的若干天
    private final String TRAINCOLNAME = "trail_seq_no"; //轨迹序列号列名
    private final String ALARMCOLNAME = "alarm_seq_no"; //报警序列号列名
    private final String TRAILCREATEOPER = "创建轨迹表";
    private final String TRAILDROPOPER = "删除轨迹表";
    private final String ALARMCREATEOPER = "创建报警表";
    private final String ALARMDROPOPER = "删除报警表";

    private Lock lock = new ReentrantLock();
    /**
     * @Description:每天凌晨清理指定日期的表
     * @Author chengwengao
     * @Date 2017/12/4 0004 13:44
     */
//    @Scheduled(initialDelay=2000, fixedRate=6000*60*60)
    @Scheduled(cron="0 0 0 * * ?")
    public void dataClear(){
        System.out.println("当前时间：" + new Date());
        //todo:控制线程执行顺序
        //轨迹表清理
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    clearTables(TRAINCOLNAME, TRAILTABPREFIX, DIVMON, DIVDAY, TRAINCOLNAME, TRAILCOUNTPERDB, TRAILDBPREFIX, TRAILCREATEOPER, TRAILDROPOPER);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage(), e.getCause());
                }
            }
        }).start();

        //报警表清理
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clearTables(ALARMCOLNAME, ALARMTABPREFIX, DIVMON, DIVDAY, ALARMCOLNAME, ALARMCOUNTPERDB, ALARMDBPREFIX, ALARMCREATEOPER, ALARMDROPOPER);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage(), e.getCause());
                }
            }
        }).start();

    }

    /**
     * @Description:封装公共数据清理方法
     * @Author chengwengao
     * @Date 2017/12/5 0005 9:33
     */
    public void clearTables(String type, String tabPrefix, int divMon, int divDay, String colName, int countPerDB, String dbPrefix, String createOper, String dropOper){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> param = new HashMap<String, String>();  //查询参数
        String tablePrefix = DataHandle.getDelTabPrefix(type, tabPrefix, divMon, divDay);    //表前缀
        //获取当前数据库个数
        Long dbCount = dataClearService.getMaxTrailSeqNo(colName);  //最大轨迹/报警序列号
        if (type.equals(TRAINCOLNAME)){
            dbCount /=  DataHandle.getAlarmDBCount(dbCount, 6).getHead();     //轨迹表后6位，每库10w辆车的数据
        }else{
            dbCount = DataHandle.getAlarmDBCount(dbCount, 7).getHead();  //报警表后7位，每300w辆车一个库
        }

        for (int i = 1; i <= dbCount; i++){
            param.put("dbName", dbPrefix + i);
            param.put("tabNamePrefix", tablePrefix + "%");
            param.put("orderRule", type);   //排序规则
            //查询该库下将要删除的表集合
            List<Schema> schemas = dataClearService.selectTabByStrLike(param);
            for (Schema schema:schemas){
                System.out.println(param.get("dbName") + "." + schema.getTableName());
                //删除表
                dataClearService.dropTable(param.get("dbName") + "." + schema.getTableName());
                log.info(sdf.format(new Date()) + "\t" + dropOper + "：" + param.get("dbName") + "." + schema.getTableName());
                if (type.equals(TRAINCOLNAME)){
                    //创建轨迹表
                    dataClearService.createTrailTable(param.get("dbName") + "." + schema.getTableName());
                }else{
                    //创建报警表
                    dataClearService.createAlarmTable(param.get("dbName") + "." + schema.getTableName());
                }
                log.info(sdf.format(new Date()) + "\t" + createOper + "：" + param.get("dbName") + "." + schema.getTableName());
            }
        }
    }
}
