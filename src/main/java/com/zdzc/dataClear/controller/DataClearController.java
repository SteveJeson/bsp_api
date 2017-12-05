package com.zdzc.dataClear.controller;

import com.zdzc.dataClear.entity.Schema;
import com.zdzc.dataClear.service.DataClearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  * @Description: 数据清理Controller
 *  * @author chengwengao
 *  * @date 2017/12/4 0004 10:15
 *  
 */
@RestController
@RequestMapping("/dataClear")
public class DataClearController {

    @Autowired
    private DataClearService dataClearService;

    @RequestMapping(value = "/dropTab")
    public void dropTab(){
        Map<String, String> param = new HashMap<String, String>();
        param.put("dbName", "gps_1");
        param.put("tabNamePrefix", "t_gps_a2\\_%");
        List<Schema> schemas = dataClearService.selectTabByStrLike(param);
        System.out.println(schemas.size());
    }
}
