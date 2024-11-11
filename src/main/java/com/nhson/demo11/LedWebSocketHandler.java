package com.nhson.demo11;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LedWebSocketHandler extends TextWebSocketHandler {

    private static boolean ledStatus = false;
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        session.sendMessage(new TextMessage(Boolean.toString(ledStatus))); // Gửi trạng thái hiện tại cho ESP32
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if ("true".equalsIgnoreCase(payload)) {
            ledStatus = true;
        } else if ("false".equalsIgnoreCase(payload)) {
            ledStatus = false;
        }
        broadcastLedStatus(); // Gửi trạng thái mới cho tất cả các client kết nối
    }

    private void broadcastLedStatus() throws IOException {
        TextMessage message = new TextMessage(Boolean.toString(ledStatus));
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(message);
            }
        }
    }

    // Phương thức để cập nhật trạng thái từ REST API
    public void setLedStatus(boolean status) throws IOException {
        ledStatus = status;
        broadcastLedStatus();
    }

    public boolean getLedStatus() {
        return ledStatus;
    }
}