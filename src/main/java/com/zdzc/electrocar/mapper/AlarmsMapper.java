package com.zdzc.electrocar.mapper;

import com.zdzc.electrocar.entity.AlarmsEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/12 0012.
 */
public interface AlarmsMapper {
    List<AlarmsEntity> getAlarmsByDeviceCodeAndTimeAndAlarmType(Map<String,Object> param);
}
