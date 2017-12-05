package com.zdzc.electrocar.service.impl;

import com.zdzc.electrocar.common.CommonBusiness;
import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.dto.GPSNapshotDto;
import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import com.zdzc.electrocar.mapper.GpsMainEntityMapper;
import com.zdzc.electrocar.mapper.GpsSnapshotEntityMapper;
import com.zdzc.electrocar.service.GpsSnapshotService;
import com.zdzc.electrocar.util.GPSConvertion;
import com.zdzc.electrocar.util.JSONResult;
import com.zdzc.electrocar.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
public class GpsSnapshotServiceImpl implements GpsSnapshotService {

    @Autowired
    private GpsSnapshotEntityMapper snapshotMapper;

    @Override
    public JSONResult selectByDeviceCode(String deviceCode) {
        JSONResult jsonResult = Const.Public.JSON_RESULT;
        if (!StringUtils.isEmpty(deviceCode)){
            GpsSnapshotEntity snapshotEntity= snapshotMapper.selectByPrimaryKey(deviceCode);
            if (snapshotEntity != null){
                GPSNapshotDto gpsNapshotDto = copySnapshotToDto(snapshotEntity);
                jsonResult.setData(gpsNapshotDto);
                return JSONResult.getResult(jsonResult, true, StatusCode.OK, Const.Public.SUCCESS);
            }
        }
        return JSONResult.getResult(jsonResult, false, StatusCode.EMPTY, Const.Public.NO_RESULT);
    }

    @Override
    public JSONResult selectByDeviceCodes(List<String> deviceCodes) {
        JSONResult jsonResult = Const.Public.JSON_RESULT;
        if (!CollectionUtils.isEmpty(deviceCodes)){
            List<GpsSnapshotEntity> snapshotEntityList = snapshotMapper.selectByDeviceCodes(deviceCodes);
            if (!CollectionUtils.isEmpty(snapshotEntityList)){
                List<GPSNapshotDto> dtos = new ArrayList<>();
                for (GpsSnapshotEntity gpsSnapshotEntity : snapshotEntityList){
                    GPSNapshotDto gpsNapshotDto = copySnapshotToDto(gpsSnapshotEntity);
                    dtos.add(gpsNapshotDto);
                }
                jsonResult.setData(dtos);
                return JSONResult.getResult(jsonResult, true, StatusCode.OK, Const.Public.SUCCESS);
            }
        }
        return JSONResult.getResult(jsonResult, false, StatusCode.EMPTY, Const.Public.NO_RESULT);
    }

    @Override
    public GPSNapshotDto copySnapshotToDto(GpsSnapshotEntity snapshotEntity) {
        if (snapshotEntity != null){
            GPSNapshotDto dto = new GPSNapshotDto();
            dto.setDeviceId(snapshotEntity.getDeviceCode());//终端设备号
            double lon = snapshotEntity.getLon();
            double lat = snapshotEntity.getLat();
            dto.setLng(lon);//经度
            dto.setLat(lat);//纬度
            //转换成高德经纬度
//            double[] gps = CommonBusiness.getGaodeGPS(lon, lat);
            double[] gps = GPSConvertion.gps84_to_gcj02(lon, lat);
            dto.setOlng(lon!=0?gps[0]:lon);
            dto.setOlat(lat!=0?gps[1]:lat);

            dto.setTime(new Date(snapshotEntity.getTime()*1000));//采集时间
            dto.setSpeed(snapshotEntity.getSpeed());//速度
            dto.setOnline("1");//在线状态，默认为1
            dto.setMile(snapshotEntity.getMile());
            //TODO 设防状态先默认设为1 1:设防 0:撤防
            dto.setFortifyStatus((byte)1);
            dto.setAccStatus(CommonBusiness.getAccStatus(snapshotEntity.getAlarmStatus()));
            return dto;
        }
        return null;
    }
}
