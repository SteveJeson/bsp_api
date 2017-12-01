package com.zdzc.dataClear.mapper;

import com.zdzc.dataClear.entity.Schema;

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
}
