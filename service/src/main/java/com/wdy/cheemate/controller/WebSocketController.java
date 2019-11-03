//package com.wdy.cheemate.controller;
//
//import com.wdy.cheemate.entity.output.WebSocketMessage;
//import lombok.AllArgsConstructor;
//import org.springframework.messaging.handler.annotation.*;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//
///**
// * @author: dongyang_wu
// * @create: 2019-10-23 18:51
// * @description:
// */
//@Controller
//@AllArgsConstructor
//public class WebSocketController {
//    private SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/sendPublicMessage")
//    //这里是客户端发送消息对应的路径，等于configureMessageBroker中配置的setApplicationDestinationPrefixes + 这路径即 /app/sendPublicMessage
//    @SendTo("/topic/public") //也可以使用 messagingTemplate.convertAndSend(); 推送
//    public WebSocketMessage sendPublicMessage(@Payload WebSocketMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/sendPrivateMessage")
//    //这里是客户端发送消息对应的路径，等于configureMessageBroker中配置的setApplicationDestinationPrefixes + 这路径即 /app/sendPrivateMessage
//    public void sendPrivateMessage(@Payload WebSocketMessage msg, Principal principal) {
//        System.out.println(principal.getName());
//         /*使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为
//            "/user/" + 用户Id + "/message",其中"/user"是固定的*/
//        //将消息推送到指定路径上
//        messagingTemplate.convertAndSendToUser(principal.getName(), "/privateMessage", msg);
//    }
//
//}