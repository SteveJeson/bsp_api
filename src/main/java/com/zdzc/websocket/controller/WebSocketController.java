package com.zdzc.websocket.controller;



import com.zdzc.dataClear.entity.SeqNoAttr;
import com.zdzc.dataClear.service.DataClearService;
import com.zdzc.websocket.bean.Message;
import com.zdzc.websocket.bean.Response;
import com.zdzc.websocket.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:websocket控制器
 * @Author chengwengao
 * @Date 2017/12/6 0006 11:36
 */
@CrossOrigin
@Controller
public class WebSocketController {
    @Autowired
    private WebSocketService ws;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    private DataClearService dataClearService;


    @RequestMapping(value = "/login")
    public String login(){
        return  "websocket/login";
    }

    @RequestMapping(value = "/ws")
    public String ws(){
        return  "websocket/ws";
    }

    @RequestMapping(value = "/chat")
    public String chat(){
        return  "websocket/chat";
    }

    @MessageMapping("/welcome")//浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    @SendTo("/topic/getResponse")//服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
    public Response say(Message message) throws Exception {
        Thread.sleep(1000);
        return new Response("Welcome, " + message.getName() + "!");
    }

    @RequestMapping("/Welcome1")
    @ResponseBody
    public String say2()throws Exception
    {
        ws.sendMessage();
        return "is ok";
    }

    @MessageMapping("/chat")
    //在springmvc 中可以直接获得principal,principal 中包含当前用户的信息
    public void handleChat(Principal principal, Message message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**
         * 此处是一段硬编码。如果发送人是wyf 则发送给 wisely 如果发送人是wisely 就发送给 wyf。
         * 通过当前用户,然后查找消息,如果查找到未读消息,则发送给当前用户。
         */
        if (principal.getName().equals("cwg")) {
            //通过convertAndSendToUser 向用户发送信息,
            // 第一个参数是接收消息的用户,第二个参数是浏览器订阅的地址,第三个参数是消息本身

            messagingTemplate.convertAndSendToUser("cfx",
                    "/queue/notifications", principal.getName() + "-send:"
                            + message.getName());
            /**
             * 72 行操作相等于 
             * messagingTemplate.convertAndSend("/user/abel/queue/notifications",principal.getName() + "-send:"
             + message.getName());
             */
        } else {
            SeqNoAttr seqNoAttr = dataClearService.pushAlarm("trail_seq_no");
            while (true){

                try {
                    if (seqNoAttr.isPushAlarm()) {
                        messagingTemplate.convertAndSendToUser("cwg",
                                "/queue/notifications", sdf.format(new Date()) + "\t" + principal.getName() + "-send:"
                                        + "超限："+seqNoAttr.getTail());
                    }else{
                        messagingTemplate.convertAndSendToUser("cwg",
                                "/queue/notifications", sdf.format(new Date()) + "\t" + principal.getName() + "-send:"
                                        + "未超限："+seqNoAttr.getTail());
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
