package com.nhson.demo11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LedWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LedWebSocketHandler.class);

    private static boolean ledStatus = false;
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        LOG.info("New WebSocket connection established. Total clients: {}", sessions.size());

        // Gửi trạng thái hiện tại cho client mới kết nối
        session.sendMessage(new TextMessage(Boolean.toString(ledStatus)));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        LOG.info("Received message from client: {}", payload);

        if ("true".equalsIgnoreCase(payload)) {
            ledStatus = true;
        } else if ("false".equalsIgnoreCase(payload)) {
            ledStatus = false;
        }

        // Phát thông điệp cập nhật trạng thái LED tới tất cả client
        broadcastLedStatus();
    }

    private void broadcastLedStatus() throws IOException {
        TextMessage message = new TextMessage(Boolean.toString(ledStatus));

        LOG.info("Broadcasting LED status to {} clients: {}", sessions.size(), ledStatus);

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
            }
        }
    }

    // Phương thức để cập nhật trạng thái từ REST API
    public void setLedStatus(boolean status) throws IOException {
        ledStatus = status;
        LOG.info("LED status updated from REST API to: {}", ledStatus);
        broadcastLedStatus();
    }

    public boolean getLedStatus() {
        return ledStatus;
    }
}
