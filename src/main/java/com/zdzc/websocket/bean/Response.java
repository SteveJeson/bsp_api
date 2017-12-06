package com.zdzc.websocket.bean;

/**
 * @Description:服务器向浏览器发送的此类消息。
 * @Author chengwengao
 * @Date 2017/12/6 0006 11:35
 */
public class Response {

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    private String responseMessage;
    public Response(String responseMessage){
        this.responseMessage = responseMessage;
    }
    public String getResponseMessage(){
        return responseMessage;
    }
}