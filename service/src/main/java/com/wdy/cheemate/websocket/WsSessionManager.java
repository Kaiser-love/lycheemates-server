package com.wdy.cheemate.websocket;

import com.wdy.cheemate.common.constant.StateConstant;
import com.wdy.cheemate.common.constant.TableConstant;
import com.wdy.cheemate.common.util.SingletonEnum;
import com.wdy.cheemate.common.util.SqlUtil;
import com.wdy.cheemate.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wdy
 * @create: 2019-10-29 09:16
 * @description:
 */
@Slf4j
public class WsSessionManager {
    /**
     * 保存连接 session 的地方
     */
    private static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     *
     * @param key
     */
    public static void add(String key, WebSocketSession session) {
        // 添加 session
        SESSION_POOL.put(key, session);
    }

    /**
     * 删除 session,会返回删除的 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession remove(String key) {
        // 删除 session
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     *
     * @param key
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
            } catch (IOException e) {
                // todo: 关闭出现异常处理
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession get(String key) {
        // 获得 session
        return SESSION_POOL.get(key);
    }

    /**
     * 指定Session发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void sendMessage(String sessionId, String message) {
        SESSION_POOL.forEach((key, session) -> {
            if (session.getId().equals(sessionId)) {
                sendMessage(session, message);
            }
        });
    }

    public static Boolean sendMessage(Long userId, String message) {
        for (String key : SESSION_POOL.keySet()) {
            if (key.equals(userId.toString())) {
                return sendMessage(SESSION_POOL.get(key), message);
            }
        }
        return false;
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * 实现服务器主动推送
     */
    public static Boolean sendMessage(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage("server 发送给 " + session.getAttributes().get("token") + " 消息 " + message + " " + LocalDateTime.now().toString()));
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 某用户是否在线
     */
    public static synchronized Boolean isAliveUser(Long userId) {
        if (userId == null)
            return false;
        return SESSION_POOL.contains(userId.toString());
    }

    /**
     * 推送用户上线后消息
     */
    public static void pushMessage(String userId) {
        // 找出私聊消息
        List<SingleChatMessage> singleChatMessages = SingletonEnum.INSTANCE.getSingleChatMessageService().getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID, userId).put(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.WAIT_PUBLISH).build());
        singleChatMessages.forEach(item -> SingletonEnum.INSTANCE.getSingleChatMessageService().sendMessage(item));

        // 找出添加用户消息
        List<UserFriendAddMessage> userFriendAddMessages = SingletonEnum.INSTANCE.getUserFriendAddMessageService().getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID, userId).put(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.WAIT_PUBLISH).build());
        userFriendAddMessages.forEach(item -> SingletonEnum.INSTANCE.getUserFriendAddMessageService().sendMessage(item));


        // 找出用户挑战邀请消息 通过服务通知推送
        List<UserChallengeInvitation> userChallengeInvitations = SingletonEnum.INSTANCE.getUserChallengeInvitationService().getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID, userId).put(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.WAIT_PUBLISH).build());
        userChallengeInvitations.forEach(item -> SingletonEnum.INSTANCE.getUserChallengeInvitationService().sendMessage(item));
    }
}

//package com.wdy.cheemate.websocket;
//
//import com.wdy.cheemate.common.constant.StateConstant;
//import com.wdy.cheemate.common.constant.TableConstant;
//import com.wdy.cheemate.common.util.SingletonEnum;
//import com.wdy.cheemate.common.util.SqlUtil;
//import com.wdy.cheemate.entity.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArraySet;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @program: esls-parent
// * @description:
// * @author: dongyang_wu
// * @create: 2019-07-19 20:13
// */
//@ServerEndpoint("/websocket/{userId}")
//@Component
//public class WebSocketServer {
//    public static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
//    private static final AtomicInteger onlineCount = new AtomicInteger(0);
//    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
//    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
//    private static ConcurrentHashMap<Long, Session> sessionSet = new ConcurrentHashMap<>();
//
//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(@PathParam("userId") Long userId, Session session) {
//        sessionSet.put(userId, session);
//        //加入set中
//        webSocketSet.add(this);
//        addOnlineCount();           //在线数加1
//        log.info("有新窗口开始监听: 当前在线人数为" + getOnlineCount());
//        sendMessage(session, "连接成功");
//        // 找出私聊消息
//        List<SingleChatMessage> singleChatMessages = SingletonEnum.INSTANCE.getSingleChatMessageService().getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID, userId.toString()).put(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.WAIT_PUBLISH).build());
//        singleChatMessages.forEach(item -> SingletonEnum.INSTANCE.getSingleChatMessageService().sendMessage(item));
//
//        // 找出添加用户消息
//        List<UserFriendAddMessage> userFriendAddMessages = SingletonEnum.INSTANCE.getUserFriendAddMessageService().getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID, userId.toString()).put(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.WAIT_PUBLISH).build());
//        userFriendAddMessages.forEach(item -> SingletonEnum.INSTANCE.getUserFriendAddMessageService().sendMessage(item));
//
//
//        // 找出用户挑战邀请消息 通过服务通知推送
////        List<UserChallengeInvitation> userChallengeInvitations = SingletonEnum.INSTANCE.getUserChallengeInvitationService().getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID, userId.toString()).put(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.WAIT_PUBLISH).build());
////        userChallengeInvitations.forEach(item -> SingletonEnum.INSTANCE.getUserChallengeInvitationService().sendMessage(item));
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose(@PathParam("userId") Long userId, Session session) {
//        sessionSet.remove(userId);
//        webSocketSet.remove(this);
//        subOnlineCount();           //在线数减1
//        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("收到来自窗口的信息:{}", message);
//        sendMessage(session, "收到消息，消息内容：" + message);
//    }
//
//    /**
//     * 群发消息
//     *
//     * @param message
//     * @throws IOException
//     */
//    public static void broadCastInfo(String message) {
//        sessionSet.forEach((key, session) -> {
//            if (session.isOpen()) {
//                sendMessage(session, message);
//            }
//        });
//    }
//
//    /**
//     * 指定Session发送消息
//     *
//     * @param sessionId
//     * @param message
//     * @throws IOException
//     */
//    public static void sendMessage(String sessionId, String message) {
//        sessionSet.forEach((key, session) -> {
//            if (session.getId().equals(sessionId)) {
//                sendMessage(session, message);
//            }
//        });
//    }
//
//    public static Boolean sendMessage(Long userId, String message) {
//        for (Long key : sessionSet.keySet()) {
//            if (key.equals(userId)) {
//                return sendMessage(sessionSet.get(key), message);
//            }
//        }
//        return false;
//    }
//
//    /**
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("发生错误");
//        error.printStackTrace();
//    }
//
//    /**
//     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
//     * 实现服务器主动推送
//     */
//    public static Boolean sendMessage(Session session, String message) {
//        try {
//            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)", message, session.getId()));
//        } catch (IOException e) {
//            log.error("发送消息出错：{}", e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//
//    /**
//     * 群发自定义消息
//     */
//    public static void sendInfo(String message, @PathParam("sid") String sid) {
//        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
//        sessionSet.forEach((key, session) -> {
//            if (session.isOpen()) {
//                sendMessage(session, message);
//            }
//        });
//    }
//
//    private static synchronized AtomicInteger getOnlineCount() {
//        return onlineCount;
//    }
//
//    private static synchronized void addOnlineCount() {
//        // 在线数加1
//        onlineCount.incrementAndGet();
//    }
//
//    private static synchronized void subOnlineCount() {
//        onlineCount.decrementAndGet();
//    }
//
//    /**
//     * 某用户是否在线
//     */
//    public static synchronized Boolean isAliveUser(Long userId) {
//        if (userId == null)
//            return false;
//        return sessionSet.contains(userId);
//    }
//}