package com.zdzc.electrocar.service;

import com.zdzc.electrocar.entity.GpsMainEntity;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public interface GpsMainService {

    GpsMainEntity selectByDeviceCode(String deviceCode);
}
