package com.zdzc.dataClear.service.impl;

import com.zdzc.dataClear.entity.Schema;
import com.zdzc.dataClear.mapper.DataClearMapper;
import com.zdzc.dataClear.service.DataClearService;
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
}
