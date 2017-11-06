package com.zdzc.electrocar.service.impl;

import com.zdzc.electrocar.common.Const;
import com.zdzc.electrocar.entity.GpsMainEntity;
import com.zdzc.electrocar.mapper.GpsMainEntityMapper;
import com.zdzc.electrocar.service.GpsMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GpsMainServiceImpl implements GpsMainService {

    @Autowired
    private GpsMainEntityMapper mainEntityMapper;

    @Override
    public GpsMainEntity selectByDeviceCode(String deviceCode) {

        return mainEntityMapper.selectByDeviceCode(deviceCode);
    }
}
