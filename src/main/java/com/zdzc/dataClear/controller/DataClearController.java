package com.zdzc.dataClear.controller;

import com.zdzc.dataClear.entity.DBRevertAttr;
import com.zdzc.dataClear.util.DataHandle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public String dbRevert(@RequestParam("file") MultipartFile multipartFile,
                           HttpServletRequest  request, HttpServletResponse response, DBRevertAttr dr){
//        DataHandle.dbRevert("127.0.0.1", "root", "123456", "gps_main1", "F:\\项目备份", "gps_main", "2017-12-07");
        System.out.println(multipartFile.getOriginalFilename());
        return DataHandle.dbRevert(dr.getHostName(), dr.getUsername(), dr.getPasswd(), dr.getDatabaseName(), multipartFile);
    }

    @RequestMapping("/dbBackUp")
    @ResponseBody
    public String dbBackUp(){
        //todo:参数配置化
        return DataHandle.dbBackup("127.0.0.1", "root", "123456", "gps_main", "F:\\项目备份");
    }
}
