package com.nhson.demo11;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LedWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LedWebSocketHandler.class);

    private static Led led = new Led(false, new Date());
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();  // Sử dụng CopyOnWriteArrayList

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);  // Thêm session vào CopyOnWriteArrayList
        LOG.info("New WebSocket connection established. Total clients: {}", sessions.size());

        // Gửi trạng thái hiện tại cho client mới kết nối
        session.sendMessage(new TextMessage(led.toString()));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        LOG.info("Received message from client: {}", payload);

        if ("true".equalsIgnoreCase(payload)) {
            led.setOn(true);
        } else if ("false".equalsIgnoreCase(payload)) {
            led.setOn(false);
        }

        // Phát thông điệp cập nhật trạng thái LED tới tất cả client
        broadcastLedStatus();
    }

    private void broadcastLedStatus() throws IOException {
        TextMessage message = new TextMessage(led.toString());

        LOG.info("Broadcasting LED status to {} clients: {}", sessions.size(), led.toString());

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                    LOG.info("Sent LED status to client: {}", session.getId());
                } catch (IOException e) {
                    LOG.error("Failed to send message to client: {}", session.getId(), e);
                }
            } else {
                LOG.warn("Skipping closed session: {}", session.getId());
                sessions.remove(session);
            }
        }
    }

    // Phương thức để cập nhật trạng thái từ REST API
    public void setLedStatus(boolean status) throws IOException {
        led.setOn(status);
        LOG.info("LED status updated from REST API to: {}", status);
        broadcastLedStatus();
    }

    public boolean getLedStatus() {
        return led.isOn();
    }

}
