package com.zdzc.electrocar.service.impl;

import com.github.pagehelper.PageHelper;
import com.zdzc.electrocar.common.CommonBusiness;
import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.dto.AlarmDto;
import com.zdzc.electrocar.dto.RequestParamDto;
import com.zdzc.electrocar.entity.AlarmEntity;
import com.zdzc.electrocar.entity.AlarmsEntity;
import com.zdzc.electrocar.entity.GpsMainEntity;
import com.zdzc.electrocar.mapper.AlarmEntityMapper;
import com.zdzc.electrocar.mapper.AlarmsMapper;
import com.zdzc.electrocar.mapper.GpsMainEntityMapper;
import com.zdzc.electrocar.service.AlarmService;
import com.zdzc.electrocar.service.GpsMainService;
import com.zdzc.electrocar.util.DateUtil;
import com.zdzc.electrocar.util.JSONResult;
import com.zdzc.electrocar.util.StatusCode;
import com.zdzc.electrocar.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmEntityMapper mapper;

    @Autowired
    private AlarmsMapper alarmsMapper;

    @Autowired
    private GpsMainEntityMapper mainEntityMapper;

    @Autowired
    private GpsMainService gpsMainService;

    /**
     * 获取设备报警信息
     * @param deviceId  设备号
     * @param alarmType 报警类型 0：未处理 1：已处理
     * @return
     */
    @Override
    public List<AlarmEntity> getAlarmListForDevice(String deviceId, String alarmType) {
        return this.mapper.selectAlarmsForDevice(deviceId, Integer.valueOf(alarmType));
    }

    /**
     * 将Entity类信息复制到Dto类以返回指定信息
     * @param entities
     * @return
     */
    @Override
    public List<AlarmDto> copyAlarmEntityToDto(List<AlarmEntity> entities) {
        if (entities != null && entities.size() > 0) {
            List<AlarmDto> dtos = new ArrayList<>(entities.size());
            for (int i = 0;i < entities.size();i++) {
                AlarmEntity entity = entities.get(i);
                AlarmDto dto = new AlarmDto();
                dto.setDeviceId(entity.getDeviceId());
                //TODO 报警状态解析  车辆状态解析,暂时默认设置为原值
                dto.setAlarmStatus(entity.getAlarmStatus());
                dto.setVehicleStatus(entity.getVehicleStatus());

                dto.setHeight(entity.getHeight());
                dto.setLongitude(entity.getLongitude());
                dto.setLatitude(entity.getLatitude());
                dto.setSpeed(entity.getSpeed());
                dto.setTime(entity.getTime());
                dto.setVendor(entity.getVendor());
                dtos.add(dto);
            }
            return dtos;
        }
        return null;
    }

    @Override
    public JSONResult getAlarmsByDeviceCodeAndTimeAndAlarmType(RequestParamDto paramDto) throws Exception {
        JSONResult jsonResult = Const.Public.JSON_RESULT;
        if (paramDto != null && !StringUtils.isEmpty(paramDto.getDeviceCode())){
            GpsMainEntity mainEntity = gpsMainService.selectByDeviceCode(paramDto.getDeviceCode());
            if (mainEntity != null){
                Map<String, Object> param = new HashMap<>();
                param.put(Const.Fields.DEVICE_CODE, paramDto.getDeviceCode());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Const.Date.Y_M_D_HMS);
                String startTime = paramDto.getBeginTime();
                String endTime = paramDto.getEndTime();
                param.put(Const.Fields.BEGIN_TIME, startTime);
                param.put(Const.Fields.END_TIME, endTime);
                param.put(Const.Fields.ALARM_HANDLE, paramDto.getAlarmHandle());
                Integer alarmSeqNo = mainEntity.getAlarmSeqNo();
                String dbName = CommonBusiness.getAlarmDbName(alarmSeqNo);
                if (!StringUtil.isEmpty(dbName)) {
                    param.put(Const.DateBase.DB_NAME, dbName);
                    // 判断查询区间是否在同一月内
//                    Date startTime = simpleDateFormat.parse(paramDto.getBeginTime());
//                    Date endTime = simpleDateFormat.parse(paramDto.getEndTime());
                    if (DateUtil.isSameMonth(startTime, endTime)){
                        String tableName = CommonBusiness.getAlarmTableName(alarmSeqNo, startTime);
                        if (!StringUtil.isEmpty(tableName)) {
                            param.put(Const.DateBase.TABLE_NAME, tableName);
                            PageHelper.startPage(paramDto.getPageNo(), paramDto.getPageSize());
                            List<AlarmsEntity> alarms = alarmsMapper.getAlarmsByDeviceCodeAndTimeAndAlarmType(param);
                            if (!CollectionUtils.isEmpty(alarms)){
                                List<AlarmDto> alarmDtos = copyAlarmsEntityToDto(alarms);
                                jsonResult.setData(alarmDtos);
                                return JSONResult.getResult(jsonResult,true, StatusCode.OK, Const.Public.SUCCESS);
                            }
                        }
                    }else {
                        return JSONResult.getResult(jsonResult, false, StatusCode.ERROR, Const.Public.NOT_IN_THE_SAME_MONTH);
                    }
                }
            }
        }
        return JSONResult.getResult(jsonResult, true, StatusCode.EMPTY, Const.Public.NO_RESULT);
    }

    @Override
    public List<AlarmDto> copyAlarmsEntityToDto(List<AlarmsEntity> alarms) throws Exception{
        if (!CollectionUtils.isEmpty(alarms)) {
            List<AlarmDto> dtos = new ArrayList<>(alarms.size());
            for (int i = 0;i < alarms.size();i++) {
                AlarmsEntity entity = alarms.get(i);
                AlarmDto dto = new AlarmDto();
                dto.setDeviceId(entity.getDeviceCode());
                //TODO 报警状态解析  车辆状态解析,暂时默认设置为原值
                dto.setAlarmStatus(entity.getAlarmStatus());
                dto.setVehicleStatus(entity.getVehicleStatus());

                dto.setHeight(entity.getHeight());
                dto.setLongitude(entity.getLon() / 1000000);
                dto.setLatitude(entity.getLat() / 1000000);
                dto.setSpeed(entity.getSpeed() / 10);
                dto.setTime(DateUtil.transDateTimeStrToDate(String.valueOf(entity.getTime())));
                dto.setVendor(StringUtils.isEmpty(entity.getVendorCode())?0:Integer.valueOf(entity.getVendorCode()));
                dtos.add(dto);
            }
            return dtos;
        }
        return null;
    }
}
