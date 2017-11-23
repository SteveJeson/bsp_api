package com.zdzc.electrocar.service;


import com.zdzc.electrocar.dto.GPSNapshotDto;
import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import com.zdzc.electrocar.util.JSONResult;

public interface GpsSnapshotService {
    JSONResult selectByDeviceCode(String deviceCode);

    GPSNapshotDto copySnapshotToDto(GpsSnapshotEntity snapshotEntity);
}
