package com.zdzc.websocket.service;

/**
 * @Description:websocket服务层
 * @Author chengwengao
 * @Date 2017/12/6 0006 11:37
 */

import com.zdzc.websocket.bean.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    //使用SimpMessagingTemplate 向浏览器发送消息
    private SimpMessagingTemplate template;

    public void sendMessage() throws Exception{
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<10;i++)
        {
            template.convertAndSend("/topic/getResponse",new Response("Welcome,cwg !"+i));
            System.out.println("----------------------cwg"+i);
            sb.append("----------------------cwg"+i+"<br />");
        }
//        return sb.toString();
    }

}
