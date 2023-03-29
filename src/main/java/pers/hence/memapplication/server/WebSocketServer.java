package pers.hence.memapplication.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 19:55
 * @description websocket服务端
 */
@ServerEndpoint("/server/{token}")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 当前会话,保证线程安全,每个线程存储该变量副本
     */
    private static final ThreadLocal<Session> sessions = new ThreadLocal<>();

    /**
     * 当前用户ID
     */
    private String token = "";

    /**
     * token:Session存放所有连接的用户会话
     */
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功
     * @param session 会话
     * @param token user ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 设置当前会话
        sessions.set(session);
        // 设置当前用户标识
        this.token = token;
        // 加入Map
        sessionMap.remove(token);
        sessionMap.put(token, session);
        log.info("新连接加入: {}, session id: {}, 当前连接数: {}", token, session.getId(), sessionMap.size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(token);
        sessions.remove();
        System.out.println(session.getId());
        log.info("用户: {} 断开连接, 当前在线人数: {}", token, sessionMap.size());
    }

    /**
     * 客户端发送消息
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * 连接错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误", error);
    }

    /**
     * 向某个session发送文本消息
     * @param message 服务端发送文本消息
     * @param session 会话
     * @throws IOException IO异常
     */
    private void sendTxtMessage(String message, Session session) throws IOException {
        if (session == null) {
            return;
        }
        session.getBasicRemote().sendText(message);
    }

    /**
     * 服务端向在线的客户端群发提醒消息
     * @param tokens UserID list
     * @param message 提醒消息
     */
    public void sendInfo(Set<String> tokens, String message) {
        if (tokens.isEmpty()) {
            return;
        }
        for (String token : tokens) {
            Session session = Optional.ofNullable(sessionMap.get(token)).orElse(null);
            try {
                // 发送消息
                sendTxtMessage(message, session);
            } catch (IOException e) {
                log.error("{} 推送消息失败: {}", token, e.getMessage());
            }
        }
    }

    /**
     * 服务端向客户端发送个性化提醒消息
     * @param map map
     */
    public void sendInfo(Map<String, String> map) {
        if (map.isEmpty()) {
            return;
        }
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String token = entry.getKey();
            String message = entry.getValue();
            Session session = Optional.ofNullable(sessionMap.get(token)).orElse(null);
            try {
                sendTxtMessage(message, session);
            } catch (IOException e) {
                log.error("{} 推送消息失败: {}", token, e.getMessage());
            }
        }
    }
}
