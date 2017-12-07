package com.zdzc.dataClear.controller;

import com.zdzc.dataClear.entity.DBRevertAttr;
import com.zdzc.dataClear.util.DataHandle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  * @Description: 数据清理Controller
 *  * @author chengwengao
 *  * @date 2017/12/4 0004 10:15
 *  
 */
@Controller
@RequestMapping("/dataClear")
public class DataClearController {

    /**
     * @Description:跳转到数据库还原页面
     * @Author chengwengao
     * @Date 2017/12/7 0007 16:09
     */
    @RequestMapping("dbRevertPage")
    public String dbRevertPage(){
        return "dbRevert";
    }
    /**
     * @Description:执行数据库还原
     * @Author chengwengao
     * @Date 2017/12/7 0007 15:55
     */
    @RequestMapping("/dbRevert")
    @ResponseBody
    public String dbRevert(DBRevertAttr dr){
//        DataHandle.dbRevert("127.0.0.1", "root", "123456", "gps_main1", "F:\\项目备份", "gps_main", "2017-12-07");
        return DataHandle.dbRevert(dr.getHostName(), dr.getUsername(), dr.getPasswd(), dr.getDatabaseName(), dr.getSourcePath(), dr.getDbBackPrefix(), dr.getRevertDate());
    }
}
