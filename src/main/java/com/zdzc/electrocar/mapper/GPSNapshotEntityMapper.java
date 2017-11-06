package com.zdzc.electrocar.mapper;

import com.zdzc.electrocar.entity.GPSNapshotEntity;
import java.util.List;

public interface GPSNapshotEntityMapper {

    /**
     * 获取设备坐标轨迹
     * @param deviceId
     * @return
     */
    List<GPSNapshotEntity> selectLatestGPSListByDeviceId(String deviceId);
}