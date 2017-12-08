package com.zdzc.electrocar.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdzc.electrocar.common.CommonBusiness;
import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.dto.GPSDto;
import com.zdzc.electrocar.dto.RequestParamDto;
import com.zdzc.electrocar.entity.GpsMainEntity;
import com.zdzc.electrocar.entity.TrailEntity;
import com.zdzc.electrocar.mapper.GpsMainEntityMapper;
import com.zdzc.electrocar.mapper.TrailMapper;
import com.zdzc.electrocar.service.GpsMainService;
import com.zdzc.electrocar.service.TrailService;
import com.zdzc.electrocar.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/9/12 0012.
 */
@Service
public class TrailServiceImpl implements TrailService {

    @Autowired
    private TrailMapper trailMapper;

    @Autowired
    private GpsMainEntityMapper mainEntityMapper;

    @Autowired
    private GpsMainService gpsMainService;

    @Override
    public JSONResult selectByDeviceCodeAndTime(RequestParamDto paramDto) throws Exception {
        JSONResult jsonResult = Const.Public.JSON_RESULT;
        if (paramDto != null && !StringUtils.isEmpty(paramDto.getDeviceCode())){
            GpsMainEntity mainEntity = gpsMainService.selectByDeviceCode(paramDto.getDeviceCode());
            if (mainEntity != null){
                Map<String, Object> param = new HashMap<>();
                param.put(Const.Fields.DEVICE_CODE, paramDto.getDeviceCode());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Const.Date.Y_M_D_HMS);
                String startTime = paramDto.getBeginTime();
                String endTime = paramDto.getEndTime();
                param.put(Const.Fields.BEGIN_TIME, startTime);
                param.put(Const.Fields.END_TIME, endTime);
                Integer trailSeqNo = mainEntity.getTrailSeqNo();
                String dbName = CommonBusiness.getTrailDbName(trailSeqNo);
                if (!StringUtil.isEmpty(dbName)) {
                    param.put(Const.DateBase.DB_NAME, dbName);
//                    Date startTime = simpleDateFormat.parse(paramDto.getBeginTime());
//                    Date endTime= simpleDateFormat.parse(paramDto.getEndTime());
                    // 判断查询区间是否在同一天
                    if (DateUtil.isSameDay(startTime, endTime)){
                        String tableName = CommonBusiness.getTrailTableName(trailSeqNo, startTime);
                        if (!StringUtil.isEmpty(tableName)) {
                            param.put(Const.DateBase.TABLE_NAME, tableName);
                            if (paramDto.getPageNo() != null && paramDto.getPageSize() != null) {
                                PageHelper.startPage(paramDto.getPageNo(), paramDto.getPageSize());
                            }
                            Page<TrailEntity> pageInfo = trailMapper.selectByDeviceCodeAndTime(param);
                            List<TrailEntity> trails = pageInfo.getResult();
                            if (!CollectionUtils.isEmpty(trails)){
                                List<GPSDto> dtos = copyTrailToGPSDto(trails, paramDto.getFilterTrails(), paramDto.getGaode());
                                jsonResult.setData(dtos);
                                return JSONResult.getResult(jsonResult, true, StatusCode.OK, Const.Public.SUCCESS);
                            }
                        }
                    }else {
                        return JSONResult.getResult(jsonResult, false, StatusCode.ERROR, Const.Public.NOT_IN_THE_SAME_DAY);
                    }
                }
            }
        }
        return JSONResult.getResult(jsonResult, false, StatusCode.EMPTY, Const.Public.NO_RESULT);
    }

    @Override
    public List<GPSDto> copyTrailToGPSDto(List<TrailEntity> trails, Boolean filterTrails, Boolean isGaode) {
        if (!CollectionUtils.isEmpty(trails)){
            List<GPSDto> dtos = new ArrayList<>();
            for (TrailEntity trail : trails){
                if (trail != null) {
                    if (!(filterTrails && (trail.getLat().equals(0.0) && trail.getLon().equals(0.0)))) {
                        double lon = trail.getLon();
                        double lat = trail.getLat();
                        GPSDto dto = new GPSDto();
                        dto.setDeviceId(trail.getDeviceCode());
                        dto.setLat(lat);
                        dto.setLng(lon);
                        //转化成高德经纬度
                        double[] gps;
                        if (isGaode){
                            gps = GPSConvertion.gps84_to_gcj02(lon, lat);
                        }else {
                            gps = CommonBusiness.getBaiduGPS(lon, lat);
                        }
                        dto.setOlng(lon != 0 ? gps[0] : lon);
                        dto.setOlat(lat != 0 ? gps[1] : lat);
                        dto.setTime(new Date(trail.getTime() * 1000));
                        dto.setSpeed(trail.getSpeed());
                        dto.setVehicleStatus(trail.getVehicleStatus());
                        dto.setAccStatus(CommonBusiness.getAccStatus(trail.getAlarmStatus()));
                        dtos.add(dto);
                    }
                }
            }
            return dtos;

        }
        return null;
    }

}
