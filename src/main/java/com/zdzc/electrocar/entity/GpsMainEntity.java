package com.zdzc.electrocar.entity;

public class GpsMainEntity {
    private Long id;

    private String deviceCode;

    private String plateNo;

    private String vendorCode;

    private Integer trailSeqNo;

    private Integer alarmSeqNo;

    private Integer status;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo == null ? null : plateNo.trim();
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode == null ? null : vendorCode.trim();
    }

    public Integer getTrailSeqNo() {
        return trailSeqNo;
    }

    public void setTrailSeqNo(Integer trailSeqNo) {
        this.trailSeqNo = trailSeqNo;
    }

    public Integer getAlarmSeqNo() {
        return alarmSeqNo;
    }

    public void setAlarmSeqNo(Integer alarmSeqNo) {
        this.alarmSeqNo = alarmSeqNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}