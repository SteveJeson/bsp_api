package com.zdzc.electrocar.service;


import com.zdzc.electrocar.dto.GPSNapshotDto;
import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import com.zdzc.electrocar.util.JSONResult;

import java.util.List;

public interface GpsSnapshotService {
    JSONResult selectByDeviceCode(String deviceCode);

    JSONResult selectByDeviceCodes(List<String> deviceCodes);

    GPSNapshotDto copySnapshotToDto(GpsSnapshotEntity snapshotEntity);
}
