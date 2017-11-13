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
        if (paramDto != null && !StringUtils.isEmpty(paramDto.getDeviceCode())){
            Map<String, Object> mainParam = new HashMap<>();
            GpsMainEntity mainEntity = gpsMainService.selectByDeviceCode(paramDto.getDeviceCode());
            if (mainEntity != null){
                Map<String, Object> param = new HashMap<>();
                param.put("deviceCode", paramDto.getDeviceCode());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                param.put("beginTime", simpleDateFormat.parse(paramDto.getBeginTime()).getTime() / 1000);
                param.put("endTime", simpleDateFormat.parse(paramDto.getEndTime()).getTime() / 1000);
                Integer trailSeqNo = mainEntity.getTrailSeqNo();
                String dbName = CommonBusiness.getTrailDbName(trailSeqNo);
                if (!StringUtil.isEmpty(dbName)) {
                    param.put("dbName", dbName);
                    // 判断查询区间是否在同一天
                    Date startTime = simpleDateFormat.parse(paramDto.getBeginTime());
                    Date finishTime = simpleDateFormat.parse(paramDto.getEndTime());
                    if (DateUtil.getYear(startTime) == DateUtil.getYear(finishTime) &&
                            DateUtil.getMonth(startTime) == DateUtil.getMonth(finishTime) &&
                            DateUtil.getDay(startTime) == DateUtil.getDay(finishTime)){
                        String tableName = CommonBusiness.getTrailTableName(trailSeqNo, startTime);
                        if (!StringUtil.isEmpty(tableName)) {
                            param.put("tableName", tableName);
                            long t1 = System.currentTimeMillis();
                            System.out.println("select start: " + t1);
                            if (paramDto.getPageNumber() != null && paramDto.getPageSize() != null) {
                                PageHelper.startPage(paramDto.getPageNumber(), paramDto.getPageSize());
                            }
                            List<TrailEntity> trails = trailMapper.selectByDeviceCodeAndTime(param);
                            System.out.println("select end: " + System.currentTimeMillis() + " ,select takes: " + (System.currentTimeMillis() - t1) + "ms");
                            if (!CollectionUtils.isEmpty(trails)){
                                long t2 = System.currentTimeMillis();
                                System.out.println("transform start: " + System.currentTimeMillis());
                                List<GPSDto> dtos = copyTrailToGPSDto(trails);
                                System.out.println("transform end: " + System.currentTimeMillis() + " ,transform takes: " + (System.currentTimeMillis() - t2) + "ms");
                                return new JSONResult(true, StatusCode.OK, "请求成功", dtos);
                            }
                        }
                    }else {
                        return new JSONResult(false, StatusCode.ERROR, "查询时间请保持在同一天内");
                    }
                }
            }
        }
        return new JSONResult(false, StatusCode.EMPTY, "未找到该车辆的轨迹信息");
    }

    @Override
    public List<GPSDto> copyTrailToGPSDto(List<TrailEntity> trails) {
        if (!CollectionUtils.isEmpty(trails)){
            List<GPSDto> dtos = new ArrayList<>();
            for (TrailEntity trail : trails){
                if (trail != null) {
                    double lon = trail.getLon();
                    double lat = trail.getLat();
                    GPSDto dto = new GPSDto();
                    dto.setDeviceId(trail.getDeviceCode());
                    dto.setLat(lat);
                    dto.setLng(lon);
                    //转化成高德经纬度
//                    double[] gps = CommonBusiness.getGaodeGPS(lon,lat);
                    double[] gps = GPSConvertion.gps84_to_gcj02(lon, lat);
                    dto.setOlng(lon!=0?gps[0]:lon);
                    dto.setOlat(lat!=0?gps[1]:lat);
                    dto.setTime(new Date(trail.getTime() * 1000));
                    dto.setSpeed(trail.getSpeed());
                    dto.setAccStatus(CommonBusiness.getAccStatus(trail.getAlarmStatus()));
                    dtos.add(dto);
                }
            }
            return dtos;

        }
        return null;
    }
}
