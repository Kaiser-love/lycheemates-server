package com.wdy.cheemate.config;

import com.wdy.cheemate.websocket.HttpAuthHandler;
import com.wdy.cheemate.websocket.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

/**
 * @program: esls-parent
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-19 20:12
 */
//@Configuration
//public class WebSocketConfig {
//
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
//}
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private HttpAuthHandler httpAuthHandler;
    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(httpAuthHandler, "websocket")
                .addInterceptors(myInterceptor)
                .setAllowedOrigins("*");
    }
}

//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        //订阅广播 Broker（消息代理）名称
//        registry.enableSimpleBroker("/topic", "/user");//topic用来广播，user用来实现p2p
//        //全局使用的订阅前缀（客户端订阅路径上会体现出来）
//        registry.setApplicationDestinationPrefixes("/app/");
//        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
//        registry.setUserDestinationPrefix("/user/");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/topicServer").setAllowedOrigins("*").withSockJS();
//        registry.addEndpoint("/queueServer").setAllowedOrigins("*").withSockJS();//注册两个STOMP的endpoint，分别用于广播和点对点
//    }
//}