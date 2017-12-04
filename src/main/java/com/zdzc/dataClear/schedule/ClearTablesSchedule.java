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

    private final int TRAILCOUNTPERDB = 100000;   //单个数据库存放的车辆数量，10w
    private final String DBPREFIX = "gps_"; //数据库前缀
    private final int DIVMON = 3;   //指定向前推的若干个月
    private final int DIVDAY = 1;   //指定向前推的若干天

    /**
     * @Description:每天凌晨清理指定日期的表
     * @Author chengwengao
     * @Date 2017/12/4 0004 13:44
     */
//    @Scheduled(initialDelay=2000, fixedRate=6000*60*60)
    @Scheduled(cron="0 0 0 * * ?")
    public void clearTables(){
        System.out.println("当前时间：" + new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> param = new HashMap<String, String>();  //查询参数
        String tablePrefix = DataHandle.getDelTabPrefix(DIVMON, DIVDAY);    //表前缀
        //获取当前数据库个数
        Long dbCount = dataClearService.getMaxTrailSeqNo()/TRAILCOUNTPERDB;

        for (int i = 1; i <= dbCount; i++){
            param.put("dbName", DBPREFIX + i);
            param.put("tabNamePrefix", tablePrefix + "%");
            //查询该库下将要删除的表集合
            List<Schema> schemas = dataClearService.selectTabByStrLike(param);
            for (Schema schema:schemas){
                System.out.println(param.get("dbName") + "." + schema.getTableName());
                //删除表
                dataClearService.dropTable(param.get("dbName") + "." + schema.getTableName());
                log.info(sdf.format(new Date()) + "\t删除表：" + param.get("dbName") + "." + schema.getTableName());
                //创建表
                dataClearService.createTrailTable(param.get("dbName") + "." + schema.getTableName());
                log.info(sdf.format(new Date()) + "\t创建表：" + param.get("dbName") + "." + schema.getTableName());

            }
        }





    }
}
