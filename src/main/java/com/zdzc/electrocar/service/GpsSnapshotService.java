package com.zdzc.electrocar.service;


import com.zdzc.electrocar.dto.GPSNapshotDto;
import com.zdzc.electrocar.entity.GpsSnapshotEntity;

public interface GpsSnapshotService {
    GpsSnapshotEntity selectByDeviceCode(String deviceCode);

    GPSNapshotDto copySnapshotToDto(GpsSnapshotEntity snapshotEntity);
}
