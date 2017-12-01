package com.zdzc.electrocar.controller;

import com.alibaba.fastjson.JSON;
import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.dto.GPSDto;
import com.zdzc.electrocar.dto.GPSNapshotDto;
import com.zdzc.electrocar.dto.RequestParamDto;
import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import com.zdzc.electrocar.service.GpsSnapshotService;
import com.zdzc.electrocar.service.TrailService;
import com.zdzc.electrocar.util.JSONResult;
import com.zdzc.electrocar.util.StatusCode;
import groovy.util.IFileNameFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

@Controller
public class LocationController {

    @Autowired
    private GpsSnapshotService snapshotService;

    @Autowired
    private TrailService trailService;

    @RequestMapping("/index")
    public String init(Model model){
        return "welcome";
    }

    @RequestMapping("/position")
    public String position(){
        return "position";
    }

    @RequestMapping("/getPosition")
    @ResponseBody
    public JSONResult getPosition(@RequestParam("deviceCode") String deviceCode){
        try {
            return snapshotService.selectByDeviceCode(deviceCode);
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONResult.getResult(Const.Public.JSON_RESULT, false, StatusCode.ERROR, Const.Public.SYSTEM_ERROR);
    }

    @RequestMapping("/trail")
    public String trail(){
        return "trail";
    }

    @RequestMapping("/getTrail")
    @ResponseBody
    public JSONResult getTrail(@RequestParam("deviceCode") String deviceCode,
                         @RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime){
        try {
            RequestParamDto paramDto = new RequestParamDto();
            paramDto.setDeviceCode(deviceCode);
            paramDto.setBeginTime(startTime);
            paramDto.setEndTime(endTime);
            paramDto.setFilterTrails(true);
            JSONResult result = trailService.selectByDeviceCodeAndTime(paramDto);
            if (result != null) {
                List<GPSDto> dtos = (List<GPSDto>) result.getData();
                if (!CollectionUtils.isEmpty(dtos)){
                    Map<String, Object> data = new HashMap<>();
                    List<double[]> trails = new ArrayList<>();
                    List<Map<String, Object>> parkerPoints = new ArrayList<>();
                    for (int i = 0; i < dtos.size(); i++){
                        GPSDto gpsDto = dtos.get(i);
                        if (!Const.VehicleStatus.INVALID_POSITON.equals(gpsDto.getVehicleStatus())) {
                            double[] trail = {gpsDto.getOlng(), gpsDto.getOlat()};
                            trails.add(trail);
                        }
                        if (i > 0){
                            GPSDto dtoBefore = dtos.get(i-1);
                            long parkerTime = gpsDto.getTime().getTime() - dtoBefore.getTime().getTime();
                            if (parkerTime > 600000){
                                Map<String, Object> parkerPoint = new HashMap<>();
                                parkerPoint.put("startTime", dtoBefore.getTime());
                                parkerPoint.put("endTime", gpsDto.getTime());
                                parkerPoint.put("parkerTime",parkerTime);
                                parkerPoints.add(parkerPoint);
                            }
                        }
                    }
                    data.put("trails", trails);
                    data.put("parkerPoints", parkerPoints);
                    result.setData(data);
                    return result;
                }else {
                    return result;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONResult.getResult(Const.Public.JSON_RESULT, false, StatusCode.ERROR, Const.Public.SYSTEM_ERROR);
     }

}
