package com.zdzc.electrocar.service;

import com.zdzc.electrocar.dto.GPSDto;

import java.util.Map;

/**
 * 相关定位轨迹展示对应 Service
 */
public interface LocationService {
    Map<String, Object> genParkPoint(GPSDto dtoBefore, GPSDto dto, long parkTime);

    String calculateParkTime(long parkTime);
}
