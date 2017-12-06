package com.zdzc.dataClear.service.impl;

import com.zdzc.dataClear.entity.Schema;
import com.zdzc.dataClear.entity.SeqNoAttr;
import com.zdzc.dataClear.mapper.DataClearMapper;
import com.zdzc.dataClear.service.DataClearService;
import com.zdzc.dataClear.util.DataHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  * @Description: 类的描述
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 16:29
 *  
 */
@Service
public class DataClearServiceImpl implements DataClearService {

    private final String TRAINCOLNAME = "trail_seq_no"; //轨迹序列号列名
    private final String ALARMCOLNAME = "alarm_seq_no"; //报警序列号列名
    private final int MAXTRAIN = 90000;   //轨迹信息，单个数据库存放的最大触发报警数，9w
    private final int MAXALARM = 2900000;   //报警信息，单个数据库存放的最大触发报警数，290w

    @Autowired
    private DataClearMapper dataClearMapper;

    /**
     * @Description:根据拼接字串模糊查找表
     * @Author chengwengao
     * @Date 2017/12/4 0004 14:26
     */
    @Override
    public List<Schema> selectTabByStrLike(Map<String, String> param) {
        return dataClearMapper.selectTabByStrLike(param);
    }

    /**
      * @Description:drop tables
      * @Author chengwengao
     * @Date 2017/12/4 0004 13:53
     */
    @Override
    public int dropTable(@Param("tableName") String tableName){
        return dataClearMapper.dropTable(tableName);
    }

    /**
      * @Description:获取gps_main表最大轨迹序列号
      * @Author chengwengao
     * @Date 2017/12/4 0004 14:25
     * @param colName 列名
     */
    @Override
    public Long getMaxTrailSeqNo(String colName){
        return dataClearMapper.getMaxTrailSeqNo(colName);
    }

    /**
      * @Description:创建指定日期的轨迹表
      * @Author chengwengao
     * @Date 2017/12/4 0004 15:16
     */
    @Override
    public int createTrailTable(String tableName){
        return dataClearMapper.createTrailTable(tableName);
    }

    /**
      * @Description:创建报警表
      * @Author chengwengao
     * @Date 2017/12/4 0004 15:16
     */
    @Override
    public int createAlarmTable(String tableName){
        return dataClearMapper.createAlarmTable(tableName);
    }

    /**
      * @Description:获取main表数据量,判断是否推送报警
      * @Author chengwengao
     * @Date 2017/12/6 0006 17:47
     * @param colName 查询列名
     */
    @Override
    public SeqNoAttr pushAlarm(String colName){
        SeqNoAttr seqNoAttr = new SeqNoAttr();
        seqNoAttr.setPushAlarm(false);
        Long dataCount = this.getMaxTrailSeqNo(colName);  //最大轨迹/报警序列号
        if (colName.equals(TRAINCOLNAME)){
            seqNoAttr =  DataHandle.getAlarmDBCount(dataCount, 6);     //轨迹表后6位，每库10w辆车的数据
            if (dataCount >= MAXTRAIN){
                seqNoAttr.setPushAlarm(true);
            }
        }
        if(colName.equals(ALARMCOLNAME)){
            dataCount = DataHandle.getAlarmDBCount(dataCount, 7).getTail();  //报警表后7位，每300w辆车一个库
            if(dataCount > MAXALARM){
                seqNoAttr.setPushAlarm(true);
            }
        }
        return seqNoAttr;
    }
}
