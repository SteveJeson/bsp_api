package com.zdzc.electrocar.controller;

import com.zdzc.electrocar.common.CommonBusiness;
import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.dto.GPSDto;
import com.zdzc.electrocar.dto.RequestParamDto;
import com.zdzc.electrocar.service.GpsSnapshotService;
import com.zdzc.electrocar.service.LocationService;
import com.zdzc.electrocar.service.TrailService;
import com.zdzc.electrocar.util.DateUtil;
import com.zdzc.electrocar.util.JSONResult;
import com.zdzc.electrocar.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Controller
public class LocationController {

    @Autowired
    private GpsSnapshotService snapshotService;

    @Autowired
    private TrailService trailService;

    // 停车间隔
    @Value("${parkerInterval}")
    private int parkInterval;

    @Autowired
    private LocationService locationService;
    /**
     * @Description:默认首页
     * @Author chengwengao
     * @Date 2017/12/1 0001 13:31
     */
    @RequestMapping("/")
    public String index(){
        return "welcome";
    }

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

    @RequestMapping("/baiduTrail")
    public String baiduTrail(){
        return "baiduTrail";
    }

    @RequestMapping("/getTrail")
    @ResponseBody
    public JSONResult getTrail(@RequestParam("deviceCode") String deviceCode,
                                @RequestParam("startTime") String startTime,
                                @RequestParam("endTime") String endTime,
                                @RequestParam("isGaode") Boolean isGaode){
        try {
            RequestParamDto paramDto = new RequestParamDto();
            paramDto.setDeviceCode(deviceCode);
            paramDto.setBeginTime(startTime);
            paramDto.setEndTime(endTime);
            paramDto.setFilterTrails(true);
            paramDto.setGaode(isGaode);
            JSONResult result = trailService.selectByDeviceCodeAndTime(paramDto);
            if (result != null) {
                List<GPSDto> dtos = (List<GPSDto>) result.getData();
                if (!CollectionUtils.isEmpty(dtos)){
                    Map<String, Object> data = new HashMap<>();
                    List<double[]> trails = new ArrayList<>();
                    List<Map<String, Object>> parkerPoints = new ArrayList<>();
                    List<Map<String, Object>> startEndPoints = new ArrayList<>();
                    for (int i = 0; i < dtos.size(); i++){
                        GPSDto gpsDto = dtos.get(i);
                        double[] trail = {gpsDto.getOlng(), gpsDto.getOlat()};
                        trails.add(trail);
                        if (i > 0){
                            GPSDto dtoBefore = dtos.get(i - 1);
                            long parkTime = gpsDto.getTime().getTime() - dtoBefore.getTime().getTime();
                            if (parkTime > parkInterval){
                                Map<String, Object> parkPoint = locationService.genParkPoint(dtoBefore, gpsDto, parkTime);
                                parkerPoints.add(parkPoint);
                            }
                        }
                        if (i == 0 || i == dtos.size() - 1){//获取起点和终点的信息
                            Map<String, Object> startEndPoint = new HashMap<>();
                            startEndPoint.put(Const.Fields.BEGIN_TIME, gpsDto.getTime());
                            startEndPoint.put(Const.Fields.POSITON, CommonBusiness.getGaodeLocation(gpsDto.getOlng(), gpsDto.getOlat()));
                            startEndPoint.put(Const.Fields.LONGITUDE, gpsDto.getOlng());
                            startEndPoint.put(Const.Fields.LATITUDE, gpsDto.getOlat());
                            startEndPoints.add(startEndPoint);
                        }
                    }
                    data.put(Const.Fields.TRAILS, trails);
                    data.put(Const.Fields.PARKER_POINTS, parkerPoints);
                    data.put(Const.Fields.START_END_POINTS, startEndPoints);
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
