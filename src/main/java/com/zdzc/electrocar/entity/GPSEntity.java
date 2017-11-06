package com.zdzc.electrocar.entity;

import java.util.Date;

public class GPSEntity {
    /** 消息ID */
    private Long id;

    /** 终端设备号 */
    private String deviceId;

    /** 报警标志 4位 */
    private Integer alarmStatus;

    /** 状态 4位 */
    private Integer vehicleStatus;

    /** 经度 单位：度 */
    private Double longitude;

    /** 纬度 单位：度 */
    private Double latitude;

    /** 海拔高度，米(m) */
    private Double height;

    /** 速度km/h */
    private Double speed;

    /** 方向 0-359，正北为 0，顺时针 */
    private Double direction;

    /** GPS时间  (GMT+8 时间) */
    private Date time;

    /** 里程 km */
    private Double mile;

    /** 油量 L */
    private Double oil;

    /** 记录仪速度 km/h */
    private Double speed2;

    /** 扩展车辆信号状态位 */
    private Integer signalStatus;

    /** IO状态位 */
    private Integer ioStatus;

    /** 模拟量 */
    private String analog;

    /** 无线通信网络信号强度 */
    private Integer wifi;

    /** 定位卫星数 */
    private Integer satelliteNum;

    /** 写库时间 */
    private Date createTime;

    /** 标识（1：迈的） */
    private Integer vendor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public Integer getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(Integer vehicleStatus) {
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

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getMile() {
        return mile;
    }

    public void setMile(Double mile) {
        this.mile = mile;
    }

    public Double getOil() {
        return oil;
    }

    public void setOil(Double oil) {
        this.oil = oil;
    }

    public Double getSpeed2() {
        return speed2;
    }

    public void setSpeed2(Double speed2) {
        this.speed2 = speed2;
    }

    public Integer getSignalStatus() {
        return signalStatus;
    }

    public void setSignalStatus(Integer signalStatus) {
        this.signalStatus = signalStatus;
    }

    public Integer getIoStatus() {
        return ioStatus;
    }

    public void setIoStatus(Integer ioStatus) {
        this.ioStatus = ioStatus;
    }

    public String getAnalog() {
        return analog;
    }

    public void setAnalog(String analog) {
        this.analog = analog == null ? null : analog.trim();
    }

    public Integer getWifi() {
        return wifi;
    }

    public void setWifi(Integer wifi) {
        this.wifi = wifi;
    }

    public Integer getSatelliteNum() {
        return satelliteNum;
    }

    public void setSatelliteNum(Integer satelliteNum) {
        this.satelliteNum = satelliteNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVendor() {
        return vendor;
    }

    public void setVendor(Integer vendor) {
        this.vendor = vendor;
    }
}