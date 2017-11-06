package com.zdzc.electrocar.service;

import com.zdzc.electrocar.dto.GPSDto;
import com.zdzc.electrocar.entity.GPSEntity;

import java.util.List;

public interface GPSService {

    List<GPSEntity> getGPSInfoForPeriod(String deviceId, String beginTime, String endTime);

    List<GPSDto> copyGPSEntityToDto(List<GPSEntity> entities);
}
