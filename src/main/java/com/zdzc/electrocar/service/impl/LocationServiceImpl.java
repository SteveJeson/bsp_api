package com.zdzc.electrocar.service.impl;

import com.zdzc.electrocar.common.CommonBusiness;
import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.dto.GPSDto;
import com.zdzc.electrocar.service.LocationService;
import com.zdzc.electrocar.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/1 0001.
 */
@Service
public class LocationServiceImpl implements LocationService{

    @Override
    public Map<String, Object> genParkPoint(GPSDto dtoBefore, GPSDto dto, long parkTime) {
        Map<String, Object> parkerPoint = new HashMap<>();
        if (dtoBefore != null) {
            parkerPoint.put(Const.Fields.BEGIN_TIME, dtoBefore.getTime());
            parkerPoint.put(Const.Fields.LONGITUDE, dtoBefore.getOlng());
            parkerPoint.put(Const.Fields.LATITUDE, dtoBefore.getOlat());
            parkerPoint.put(Const.Fields.POSITON, CommonBusiness.getGaodeLocation(dtoBefore.getOlng(),dtoBefore.getOlat()));
        }
        if (dto != null) {
            parkerPoint.put(Const.Fields.END_TIME, dto.getTime());
        }
        if (parkTime != 0) {
            parkerPoint.put(Const.Fields.PARK_TIME, calculateParkTime(parkTime));
        }


        return parkerPoint;
    }

    @Override
    public String calculateParkTime(long parkTime) {
        int parkerTime = (int)parkTime / 1000;
        int hour = parkerTime / 3600;
        int hourMod = parkerTime % 3600;
        int minute = hourMod / 60;
        int second = hourMod % 60;
        StringBuilder parkerTimeStr = new StringBuilder();
        return parkerTimeStr.append(hour).append(Const.Date.HOUR).append(minute).append(Const.Date.MINUTE).append(second).append(Const.Date.SECOND).toString();
    }
}
