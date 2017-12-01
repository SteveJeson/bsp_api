package com.zdzc.dataClear.service.impl;

import com.zdzc.dataClear.entity.Schema;
import com.zdzc.dataClear.mapper.DataClearMapper;
import com.zdzc.dataClear.service.DataClearService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Override
    public List<Schema> selectTabByStrLike(Map<String, String> param) {
        return dataClearMapper.selectTabByStrLike(param);
    }
}
