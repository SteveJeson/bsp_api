package com.zdzc.electrocar.mapper;

import com.github.pagehelper.Page;
import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import com.zdzc.electrocar.entity.TrailEntity;

import java.util.List;
import java.util.Map;

public interface TrailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TrailEntity record);

    TrailEntity selectByPrimaryKey(Long id);

    List<TrailEntity> selectAll();

    int updateByPrimaryKey(TrailEntity record);

    TrailEntity selectByDeviceCode(String deviceCode);

    Page<TrailEntity> selectByDeviceCodeAndTime(Map<String, Object> paramMap);

    int insert(Map<String, Object> param);
}