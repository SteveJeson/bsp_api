package com.zdzc.electrocar.mapper;

import com.zdzc.electrocar.entity.GpsMainEntity;
import java.util.List;
import java.util.Map;

public interface GpsMainEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GpsMainEntity record);

    GpsMainEntity selectByPrimaryKey(Long id);

    List<GpsMainEntity> selectAll();

    int updateByPrimaryKey(GpsMainEntity record);

    GpsMainEntity selectByDeviceCode(String deviceCode);
}