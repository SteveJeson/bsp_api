package com.zdzc.dataClear.schedule;

import com.zdzc.dataClear.entity.DataConst;
import com.zdzc.dataClear.entity.Schema;
import com.zdzc.dataClear.service.DataClearService;
import com.zdzc.dataClear.util.DataHandle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    //数据库备份相关参数
    @Value("${dbBackUp.hostName}")
    private String hostName;

    @Value("${dbBackUp.userName}")
    private String userName;

    @Value("${dbBackUp.passwd}")
    private String passwd;

    @Value("${dbBackUp.sourceDbName}")
    private String sourceDbName;

    @Value("${dbBackUp.destinationPath}")
    private String destinationPath;

    @Autowired
    private DataClearService dataClearService;



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
        //轨迹表清理
        Thread trailThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    clearTables(DataConst.TRAINCOLNAME, DataConst.TRAILTABPREFIX, DataConst.DIVMON, DataConst.DIVDAY, DataConst.TRAINCOLNAME, DataConst.TRAILCOUNTPERDB, DataConst.TRAILDBPREFIX, DataConst.TRAILCREATEOPER, DataConst.TRAILDROPOPER);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage(), e.getCause());
                }
            }
        });

        //报警表清理
        Thread alarmThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clearTables(DataConst.ALARMCOLNAME, DataConst.ALARMTABPREFIX, DataConst.DIVMON, DataConst.DIVDAY, DataConst.ALARMCOLNAME, DataConst.ALARMCOUNTPERDB, DataConst.ALARMDBPREFIX, DataConst.ALARMCREATEOPER, DataConst.ALARMDROPOPER);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage(), e.getCause());
                }
            }
        });

        try {
            //使用join控制线程执行顺序
            trailThread.start();
            trailThread.join();
            alarmThread.start();
            alarmThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e.getCause());
        }

    }

    /**
     * @Description:封装公共数据清理方法
     * @Author chengwengao
     * @Date 2017/12/5 0005 9:33
     */
    public void clearTables(String type, String tabPrefix, int divMon, int divDay, String colName, int countPerDB, String dbPrefix, String createOper, String dropOper) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> param = new HashMap<String, String>();  //查询参数
        String tablePrefix = DataHandle.getDelTabPrefix(type, tabPrefix, divMon, divDay);    //表前缀
        //获取当前数据库个数
        Long dbCount = dataClearService.getMaxTrailSeqNo(colName);  //最大轨迹/报警序列号
        if (type.equals(DataConst.TRAINCOLNAME)){
            dbCount =  DataHandle.getAlarmDBCount(dbCount, 6).getHead();     //轨迹表后6位，每库10w辆车的数据
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
                if (type.equals(DataConst.TRAINCOLNAME)){
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

    /**
     * @Description:每天凌晨2点执行main库备份
     * @Author chengwengao
     * @Date 2017/12/7 0007 15:32
     */
//    @Scheduled(initialDelay=2000, fixedRate=6000*60*60)
    @Scheduled(cron="0 0 2 * * ?")
    public void dbBackup(){
        DataHandle.dbBackup(hostName, userName, passwd, sourceDbName, destinationPath);
    }
}
