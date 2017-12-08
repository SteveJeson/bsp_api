package com.zdzc.electrocar.service;


import com.zdzc.electrocar.dto.GPSNapshotDto;
import com.zdzc.electrocar.entity.GpsSnapshotEntity;
import com.zdzc.electrocar.util.JSONResult;

import java.util.List;

public interface GpsSnapshotService {
    JSONResult selectByDeviceCode(String deviceCode) throws Exception;

    JSONResult selectByDeviceCodes(List<String> deviceCodes) throws Exception;

    GPSNapshotDto copySnapshotToDto(GpsSnapshotEntity snapshotEntity) throws Exception;
}
