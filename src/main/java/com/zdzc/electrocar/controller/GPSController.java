package com.zdzc.electrocar.controller;

import com.alibaba.fastjson.JSON;
import com.zdzc.electrocar.common.Authentication;
import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.dto.AlarmDto;
import com.zdzc.electrocar.dto.GPSDto;
import com.zdzc.electrocar.dto.RequestParamDto;
import com.zdzc.electrocar.entity.*;
import com.zdzc.electrocar.mapper.TrailMapper;
import com.zdzc.electrocar.service.*;
import com.zdzc.electrocar.util.Command;
import com.zdzc.electrocar.util.JSONResult;
import com.zdzc.electrocar.util.StatusCode;
import com.zdzc.electrocar.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by liuw on 2017/8/29.
 */
@RestController
@RequestMapping("/bsp/api")
public class GPSController {

    @Autowired
    private GpsNapshotService gpsNapshotService;

    @Autowired
    private GPSService gpsService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private GpsMainService mainService;

    @Autowired
    private GpsSnapshotService snapshotService;

    @Autowired
    private TrailService trailService;


    private static Logger log = LoggerFactory.getLogger(GPSController.class);

    /**
     * 获取设备最新坐标信息
     * @param deviceId
     * @param token
     * @return
     */
    @RequestMapping(value="/GPSInfo/{deviceId}/{accessToken}")
    public JSONResult getLatestGPSInfoByDeviceId(@PathVariable("deviceId") String deviceId,
                                                 @PathVariable("accessToken")String token) {
        //TODO token校验通过才进行API调用
        if (Authentication.validateToken(token)) {
            GPSNapshotEntity entity =
                    gpsNapshotService.getLatestGPSInfoListByDeviceId(deviceId);
            if (entity != null) {
                return new JSONResult(true,StatusCode.OK,"获取信息成功",gpsNapshotService.copyGPSEntityToDto(entity));
            } else {
                return new JSONResult(true,StatusCode.EMPTY,"未获取到相关信息",null);
            }
        } else{
            return new JSONResult(false,StatusCode.ACCESS_DENIED,"访问被拒绝",null);
        }
    }

    /**
     * 获取设备指定时间内坐标信息
     * @param request
     * @return
     */
    @RequestMapping(value="/GPSList")
    public JSONResult getGPSListForPeriod(HttpServletRequest request) {
        String token = request.getParameter("token");
        String deviceId = request.getParameter("deviceId");
        String startTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        if (Authentication.validateToken(token)) {
            List<GPSEntity> entities =  gpsService.getGPSInfoForPeriod(deviceId, startTime, endTime);
            if (entities != null && entities.size() > 0) {
                List<GPSDto> dtos = gpsService.copyGPSEntityToDto(entities);
                return new JSONResult(true,StatusCode.OK,"获取信息成功",dtos);
            } else {
                return new JSONResult(true,StatusCode.EMPTY,"未获取到相关信息",null);
            }
        } else {
            return new JSONResult(false,StatusCode.ACCESS_DENIED,"访问被拒绝",null);
        }

    }

    /**
     * 获取设备报警信息
     * @param deviceId
     * @param alarmType
     * @return
     */
    @RequestMapping(value="/alarms/{deviceId}/{alarmType}/{token}")
    public JSONResult getAlarmListForDevice(@PathVariable("deviceId") String deviceId,
                                            @PathVariable("alarmType") String alarmType,
                                            @PathVariable("token") String token) {
        if (Authentication.validateToken(token)) {
            List<AlarmEntity> entities = alarmService.getAlarmListForDevice(deviceId, alarmType);
            if (entities != null && entities.size() > 0) {
                List<AlarmDto> dtos = alarmService.copyAlarmEntityToDto(entities);
                return new JSONResult(true, StatusCode.OK, "获取信息成功",dtos);
            } else {
                return new JSONResult(true, StatusCode.EMPTY, "未获取到相关信息", null);
            }
        } else {
            return new JSONResult(false, StatusCode.ACCESS_DENIED,"访问被拒绝",null);
        }

    }

    @RequestMapping(value = "/command/{token}",method = RequestMethod.POST)
    public JSONResult sendCommands(CommandEntity entity,@PathVariable("token") String token,HttpServletRequest request) {
        if (!Authentication.validateToken(token)) {
            return new JSONResult(false, StatusCode.ACCESS_DENIED,"访问被拒绝");
        }
        //TODO 具体业务逻辑待定
//        String deviceId = entity.getDeviceId();
        String commandType = entity.getCommand();//指令类型
        String parameter = entity.getParameter();//指令参数值
        String extendParam = entity.getExtendParam();//扩展参数
        String message;
        try{
            message = Command.valueOf(commandType).getCommand();
        } catch (Exception e) {
            message = "不识别的指令类型";
            return new JSONResult(true,0,message);
        }


        //TODO 指令发送失败的逻辑
        if (StringUtils.equals(commandType, "C1")) {
            if (StringUtils.equals(parameter, "1")) {
                if (StringUtils.isEmpty(extendParam) ||
                        (!StringUtil.isInDecScope(extendParam, "10%","100%"))) {
                    message = "限速比例未设置或超出可设置范围";
                    return new JSONResult(true,0,message);
                }
            }
        }
        return new JSONResult(true,StatusCode.OK,"指令发送成功");
    }

    @RequestMapping("/mainInfo/{deviceCode}/{token}")
    public JSONResult getMainInfo(@PathVariable("deviceCode") String deviceCode,
                                   @PathVariable("token") String token,
                                   HttpServletRequest request){
        if (Authentication.validateToken(token)) {
            GpsMainEntity mainEntity = mainService.selectByDeviceCode(deviceCode);
            if (mainEntity != null) {
                return new JSONResult(true, StatusCode.OK, "获取信息成功", mainEntity);
            }
        }
        return new JSONResult(true, StatusCode.EMPTY, "信息为空");
    }

    @RequestMapping("/snapshot")
    public JSONResult getSnapshot(@RequestParam("deviceCode") String deviceCode,
                                   @RequestParam("token") String token,
                                   HttpServletRequest request){
        try {
            if (Authentication.validateToken(token)) {
                GpsSnapshotEntity snapshotEntity = snapshotService.selectByDeviceCode(deviceCode);
                if (snapshotEntity != null) {
                    return new JSONResult(true, StatusCode.OK, Const.Public.SUCCESS, snapshotService.copySnapshotToDto(snapshotEntity));
                } else {
                    return new JSONResult(true, StatusCode.EMPTY, "数据为空");
                }
            } else {
                return new JSONResult(false, StatusCode.ERROR, Const.Public.TOKEN_ERROR);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return new JSONResult(false, StatusCode.ERROR, Const.Public.SYSTEM_ERROR);
    }

    @RequestMapping("/locationList")
    public JSONResult getLocationList(@Valid RequestParamDto paramDto,BindingResult bindingResult,
                                       HttpServletRequest request){
        JSONResult jsonResult = Const.Public.JSON_RESULT;
        try {
            if (Authentication.validateToken(paramDto.getToken())) {
                return trailService.selectByDeviceCodeAndTime(paramDto);
            }else {
                return JSONResult.getResult(jsonResult,false, StatusCode.ACCESS_DENIED, Const.Public.TOKEN_ERROR);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return JSONResult.getResult(jsonResult, false, StatusCode.ERROR, Const.Public.SYSTEM_ERROR);
    }

    @RequestMapping("/locationList1")
    public JSONResult getLocationList1(@Valid RequestParamDto paramDto,BindingResult bindingResult,
                                      HttpServletRequest request) {
//        try {
//            if (Authentication.validateToken(paramDto.getToken())) {
//                return trailService.selectByDeviceCodeAndTime(paramDto);
//            } else {
//                return new JSONResult(false, StatusCode.ACCESS_DENIED, "TOKEN 验证失败");
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }

        return new JSONResult(false, StatusCode.ERROR, "系统异常");
    }

    @RequestMapping("/locationList2")
    public JSONResult getLocationList2(@RequestParam("deviceCode") String deviceCode,
                                       HttpServletRequest request) {
//        try {
//            if (Authentication.validateToken(paramDto.getToken())) {
//                return trailService.selectByDeviceCodeAndTime(paramDto);
//            } else {
//                return new JSONResult(false, StatusCode.ACCESS_DENIED, "TOKEN 验证失败");
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }

        return null;
    }

    @RequestMapping("/alarmsList")
    public JSONResult getAlarmsList(@Valid RequestParamDto paramDto,BindingResult bindingResult,
                                     HttpServletRequest request){
        JSONResult jsonResult = Const.Public.JSON_RESULT;
        try {
            if (Authentication.validateToken(paramDto.getToken())) {
                return alarmService.getAlarmsByDeviceCodeAndTimeAndAlarmType(paramDto);
            }else {
                return JSONResult.getResult(jsonResult,false, StatusCode.ACCESS_DENIED, Const.Public.TOKEN_ERROR);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return JSONResult.getResult(jsonResult, false, StatusCode.ERROR, Const.Public.SYSTEM_ERROR);
    }

}
