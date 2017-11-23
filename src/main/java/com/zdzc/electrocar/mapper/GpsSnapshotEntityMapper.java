package com.zdzc.electrocar.mapper;

import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import java.util.List;

public interface GpsSnapshotEntityMapper {
    int deleteByPrimaryKey(String deviceCode);

    int insert(GpsSnapshotEntity record);

    GpsSnapshotEntity selectByPrimaryKey(String deviceCode);

    List<GpsSnapshotEntity> selectAll();

    int updateByPrimaryKey(GpsSnapshotEntity record);
}