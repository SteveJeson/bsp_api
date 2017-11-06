package com.zdzc.electrocar.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * dto类 只返回前端需要的字段
 */
public class GPSNapshotDto implements Serializable{

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
     * 经度(百度)
     */
    private Double blng;
    /**
     * 纬度(百度)
     */
    private Double blat;
    /**
     * 采集时间
     */
    private Date time;
    /**
     * 速度
     */
    private Double speed;
    /**
     * 在线状态
     */
    private String online;
    /**
     * 里程
     */
    private Double mile;

    /**
     * 电量
     */
    private double power;
    /**
     * 充电状态
     */
    private String powerStatus;
    /**
     * 设防状态
     */
    private byte fortifyStatus;
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

    public Double getMile() {
        return mile;
    }

    public void setMile(Double mile) {
        this.mile = mile;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(String powerStatus) {
        this.powerStatus = powerStatus;
    }

    public byte getFortifyStatus() {
        return fortifyStatus;
    }

    public void setFortifyStatus(byte fortifyStatus) {
        this.fortifyStatus = fortifyStatus;
    }

    public int getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
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

    public Double getBlng() {
        return blng;
    }

    public void setBlng(Double blng) {
        this.blng = blng;
    }

    public Double getBlat() {
        return blat;
    }

    public void setBlat(Double blat) {
        this.blat = blat;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
