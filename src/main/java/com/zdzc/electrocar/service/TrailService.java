package com.zdzc.electrocar.service;

import com.zdzc.electrocar.dto.GPSDto;
import com.zdzc.electrocar.dto.RequestParamDto;
import com.zdzc.electrocar.entity.TrailEntity;
import com.zdzc.electrocar.util.JSONResult;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/12 0012.
 */
public interface TrailService {
    JSONResult selectByDeviceCodeAndTime(RequestParamDto paramDto) throws Exception;

    List<GPSDto> copyTrailToGPSDto(List<TrailEntity> trails, Boolean filterTrails, Boolean isGaode) throws Exception;
}
