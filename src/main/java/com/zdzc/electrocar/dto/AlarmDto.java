package com.zdzc.electrocar.dto;

import java.util.Date;

public class AlarmDto {

    /** 终端设备号 */
    private String deviceId;

    /**报警状态*/
    private int alarmStatus;

    /**车辆状态*/
    private int vehicleStatus;

    /** 经度 单位：度 */
    private Double longitude;

    /** 纬度 单位：度 */
    private Double latitude;

    /** 海拔高度，米(m) */
    private Double height;

    /** 速度km/h */
    private Double speed;

    /** GPS时间  (GMT+8 时间) */
    private Date time;

    /** 标识（0：默认 1：迈的 2：沃瑞特） */
    private Integer vendor;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public int getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(int vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getVendor() {
        return vendor;
    }

    public void setVendor(Integer vendor) {
        this.vendor = vendor;
    }
}
