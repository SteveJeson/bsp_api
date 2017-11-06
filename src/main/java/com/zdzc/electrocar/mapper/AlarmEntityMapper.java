package com.zdzc.electrocar.mapper;

import com.zdzc.electrocar.entity.AlarmEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlarmEntityMapper {

    /**
     * 获取设备报警信息
     * @param deviceId
     * @param alarmType
     * @return
     */
    List<AlarmEntity> selectAlarmsForDevice(@Param("deviceId") String deviceId, @Param("alarmType") int alarmType);

}