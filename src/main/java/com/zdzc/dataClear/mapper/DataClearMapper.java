package com.zdzc.dataClear.mapper;

import com.zdzc.dataClear.entity.Schema;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 *  * @Description: 类的描述
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 16:22
 *  
 */
public interface DataClearMapper {
    /**
     * @Description:根据拼接字串模糊查找表
     * @Author chengwengao
     * @Date 2017/12/1 0001 16:27
     */
    List<Schema> selectTabByStrLike(Map<String, String> param);

    /**
     * @Description:drop tables
     * @Author chengwengao
     * @Date 2017/12/4 0004 13:53
     */
    int dropTable(@Param("tableName") String tableName);

    /**
     * @Description:获取gps_main表最大轨迹序列号
     * @Author chengwengao
     * @Date 2017/12/4 0004 14:25
     * @param colName 列名
     */
    Long getMaxTrailSeqNo(String colName);

    /**
     * @Description:创建指定日期的轨迹表
     * @Author chengwengao
     * @Date 2017/12/4 0004 15:16
     */
    int createTrailTable(String tableName);

    /**
      * @Description:创建报警表
      * @Author chengwengao
     * @Date 2017/12/4 0004 15:16
     */
    int createAlarmTable(String tableName);
}
