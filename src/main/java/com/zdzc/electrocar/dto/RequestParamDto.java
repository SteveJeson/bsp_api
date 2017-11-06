package com.zdzc.electrocar.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/21 0021.
 */
public class RequestParamDto implements Serializable{

    private String deviceCode;

    private String beginTime;

    private String endTime;

    private String token;

    private Integer alarmHandle;

    private Integer pageNumber;

    private Integer pageSize;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAlarmHandle() {
        return alarmHandle;
    }

    public void setAlarmHandle(Integer alarmHandle) {
        this.alarmHandle = alarmHandle;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
