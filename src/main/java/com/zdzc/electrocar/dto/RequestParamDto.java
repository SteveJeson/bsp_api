package com.zdzc.electrocar.dto;

import javax.persistence.criteria.Predicate;
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

    private Integer pageNo;

    private Integer pageSize;

    private Boolean filterTrails;

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

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getFilterTrails() {
        return filterTrails;
    }

    public void setFilterTrails(Boolean filterTrails) {
        this.filterTrails = filterTrails;
    }
}
