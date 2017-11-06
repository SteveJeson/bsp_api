package com.zdzc.electrocar.mapper;

import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import java.util.List;
import java.util.Map;

public interface GpsSnapshotEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GpsSnapshotEntity record);

    GpsSnapshotEntity selectByPrimaryKey(Long id);

    List<GpsSnapshotEntity> selectAll();

    int updateByPrimaryKey(GpsSnapshotEntity record);

    GpsSnapshotEntity selectByDeviceCode(String deviceCode);
}