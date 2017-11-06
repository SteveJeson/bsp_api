package com.zdzc.electrocar.entity;

public class CommandEntity {

    /**
     * 终端设备号
     */
    private String deviceId;

    /**
     * 指令类型
     */
    private String command;

    /**
     * 指令参数
     */
    private String parameter;

    /**
     * 扩展参数
     */
    private String extendParam;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }
}
