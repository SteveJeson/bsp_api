package com.zdzc.electrocar.service;

import com.zdzc.electrocar.dto.AlarmDto;
import com.zdzc.electrocar.dto.RequestParamDto;
import com.zdzc.electrocar.entity.AlarmEntity;
import com.zdzc.electrocar.entity.AlarmsEntity;
import com.zdzc.electrocar.util.JSONResult;

import java.util.List;
import java.util.Map;

public interface AlarmService {

    List<AlarmEntity> getAlarmListForDevice(String deviceId, String alarmType);

    List<AlarmDto> copyAlarmEntityToDto(List<AlarmEntity> entities);

    JSONResult getAlarmsByDeviceCodeAndTimeAndAlarmType(RequestParamDto paramDto) throws Exception;

    List<AlarmDto> copyAlarmsEntityToDto(List<AlarmsEntity> alarms) throws Exception;
}
