package com.nhson.demo11;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/led")
public class LedController {

    @Autowired
    private LedWebSocketHandler webSocketHandler;

    @PostMapping("/update")
    public ResponseEntity updateLedStatus(@RequestBody boolean status, @RequestHeader("Authorization") String token) throws IOException {
        String usernameReq;
        String passwordReq ;
        if (token.contains(":")) {
            String[] credentials = token.split(":");
            usernameReq = credentials[0];
            passwordReq = credentials[1];
            if(usernameReq.equals("admin") && passwordReq.equals("admin")){
                webSocketHandler.setLedStatus(status); // Cập nhật trạng thái và gửi cho tất cả các client WebSocket
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/status")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<Boolean> getLedStatus() {
        boolean status =webSocketHandler.getLedStatus();
        return ResponseEntity.ok(status);
    }

    @PostMapping("/update-time")
    public ResponseEntity setLedTime(@RequestBody Map<String, String> timeRequest, @RequestHeader("Authorization") String token) throws IOException {
        if (token.contains(":")) {
            String[] credentials = token.split(":");
            String usernameReq = credentials[0];
            String passwordReq = credentials[1];
            if(usernameReq.equals("admin") && passwordReq.equals("admin")) {
                LocalDateTime now = LocalDateTime.now();
                int hour = Integer.parseInt(timeRequest.get("hour"));
                int minute = Integer.parseInt(timeRequest.get("minute"));
                String timeString = hour + ":" + minute;
                webSocketHandler.setLedTime(timeString);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping
    public ResponseEntity<Led> getLedConfig(){
        Led led = new Led(webSocketHandler.getLedStatus(), webSocketHandler.getLedTime());
        return ResponseEntity.ok(led);
    }
}