package com.tuanbaol.messageserver.websocket;

import com.tuanbaol.messageserver.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0
 */
@Component
@Slf4j
public class MyWebSocketHandler extends AbstractWebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    @Autowired
    private static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * webSocket连接创建后调用
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
    }

    /**
     * 接收到消息会调用
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            log.info("get text message-{}", message);
        } else {
            logger.info("Unexpected WebSocket message type: " + message);
        }
    }

    public void send(Object result) {
        send(sessionMap.keySet(), result);
    }

    public void send(Set<String> sessionIds, Object result) {
        sessionIds.stream().forEach(sessionId -> doSend(sessionMap.get(sessionId), result));
    }

    /**
     * 连接出错会调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        try {
            log.error("websocket连接出错！", exception);
            String sessionId = session.getId();
            sessionMap.remove(sessionId);
        } catch (Exception e) {
            log.error("deal handleTransportError failed!", e);
        }
    }


    /**
     * 连接关闭会调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            String sessionId = session.getId();
            sessionMap.remove(sessionId);
        } catch (Exception e) {
            log.error("deal afterConnectionClosed failed!", e);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 后端发送消息
     */
    private void doSend(WebSocketSession session, Object message) {
        if (session.isOpen()) {
            try {
                synchronized (session) {
                    Class clazz = message.getClass();
                    if (clazz.isAssignableFrom(byte[].class)) {
                        BinaryMessage binaryMessage = new BinaryMessage((byte[]) message);
                        log.info("websocket发送byte消息-【{}】");
                        session.sendMessage(binaryMessage);
                    } else if (clazz == String.class) {
                        session.sendMessage(new TextMessage(((String) message).getBytes()));
                    } else {
                       log.warn("不支持的websocket发送数据类型，class-【{}】", clazz.getName());
                    }
                }
            } catch (IOException e) {
                throw new ServiceException("发送websocket消息失败！", e);
            }
        } else {
            log.error("发送消息时，websocket连接已断开，sessionId-【{}】", session.getId());
        }
    }
}
