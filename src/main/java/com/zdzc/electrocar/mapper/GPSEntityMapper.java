package com.zdzc.electrocar.mapper;

import com.zdzc.electrocar.entity.GPSEntity;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface GPSEntityMapper {

    /**
     * 获取某设备指定时间内坐标信息
     * @param params：deviceID startTime endTime
     * @return
     */
    List<GPSEntity> selectGPSInfoForPeriod(@Param("params") HashMap<String, Object> params);
}