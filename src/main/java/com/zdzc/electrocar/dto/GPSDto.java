package com.zdzc.electrocar.dto;

import java.io.Serializable;
import java.util.Date;

public class GPSDto implements Serializable{

    /**
     * 终端设备号
     */
    private String deviceId;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度(高德)
     */
    private Double olng;
    /**
     * 纬度(高德)
     */
    private Double olat;
    /**
     * 采集时间
     */
    private Date time;
    /**
     * 速度
     */
    private Double speed;
    /**
     * Acc状态
     */
    private int accStatus;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getOlng() {
        return olng;
    }

    public void setOlng(Double olng) {
        this.olng = olng;
    }

    public Double getOlat() {
        return olat;
    }

    public void setOlat(Double olat) {
        this.olat = olat;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public int getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
    }
}
