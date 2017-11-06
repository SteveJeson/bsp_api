package com.zdzc.electrocar.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Command {
//    C0: 防盗指令
//    C1: 限速指令
//    C2: 寻车指令
//    C3: 解链条锁
//    C4: 远程ACC

    C0("防盗指令"),
    C1("限速指令"),
    C2("寻车指令"),
    C3("解链条锁"),
    C4("远程ACC");

    String command;

    Command(String command) {
        this.command = command;
    }

    @JsonValue
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
