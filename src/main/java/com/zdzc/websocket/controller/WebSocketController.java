package com.zdzc.websocket.controller;



import com.zdzc.dataClear.entity.SeqNoAttr;
import com.zdzc.dataClear.service.DataClearService;
import com.zdzc.dataClear.util.DataHandle;
import com.zdzc.websocket.bean.Message;
import com.zdzc.websocket.bean.Response;
import com.zdzc.websocket.service.WebSocketService;
import org.apache.log4j.Logger;
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

    private static final Logger log = Logger.getLogger(WebSocketController.class);

    private final int PUSH_INTERVAL = 1000*60*60*12;   //推送时间间隔，单位毫秒
    private final String TRAINCOLNAME = "trail_seq_no"; //轨迹序列号列名
    private final String ALARMCOLNAME = "alarm_seq_no"; //报警序列号列名

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
        if (principal.getName().equals("cwg") || principal.getName().equals("cfx")) {
            //通过convertAndSendToUser 向用户发送信息,
            // 第一个参数是接收消息的用户,第二个参数是浏览器订阅的地址,第三个参数是消息本身
            SeqNoAttr seqNoAttr_trail = new SeqNoAttr();    //轨迹
            SeqNoAttr seqNoAttr_alarm = new SeqNoAttr();    //报警
            StringBuffer sb = new StringBuffer();
            int i = 0;
            while (true){

                try {
                    sb = sb.delete(0, sb.length());     //清空字符串
                    seqNoAttr_trail = dataClearService.pushAlarm(TRAINCOLNAME);
                    seqNoAttr_alarm = dataClearService.pushAlarm(ALARMCOLNAME);
                    sb.append(sdf.format(new Date()) + "\t" + principal.getName() + "推送:");
                    if (DataHandle.isNotEmpty(seqNoAttr_trail.getMsg())){
                        sb.append("<font color='red'>[位置]" + seqNoAttr_trail.getMsg()).append("</font>\t");
                        i++;
                    }else {
                        i = 0;
                        if (seqNoAttr_trail.isPushAlarm()) {
                            sb.append("<font color='red'>轨迹库超限："+seqNoAttr_trail.getTail()).append("</font>\t");
                        }else{
                            sb.append("轨迹库未超限："+seqNoAttr_trail.getTail()).append("\t");
                        }
                    }
                    if(DataHandle.isNotEmpty(seqNoAttr_alarm.getMsg())){
                        sb.append("<font color='red'>[报警]" + seqNoAttr_alarm.getMsg()).append("</font>\t");
                        i++;
                    }else{
                        i = 0;
                        if (seqNoAttr_alarm.isPushAlarm()) {
                            sb.append("<font color='red'>报警库超限："+seqNoAttr_alarm.getTail()).append("</font>\t");
                        }else{
                            sb.append("报警库未超限："+seqNoAttr_alarm.getTail()).append("\t");
                        }
                    }
                    messagingTemplate.convertAndSendToUser(principal.getName(),
                            "/queue/notifications", sb.toString());
                    log.info(sb.toString());
                    if (i > 1){
                        break;
                    }else{
                        Thread.sleep(PUSH_INTERVAL);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error(sdf.format(new Date()) + "\t" + e.getMessage(), e.getCause());
                }
            }

        } else {
            messagingTemplate.convertAndSendToUser(principal.getName(),
                    "/queue/notifications", "您无权查看！");

        }
    }

}
