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
    public ResponseEntity updateLedStatus(@RequestBody boolean status) throws IOException {
        webSocketHandler.setLedStatus(status); // Cập nhật trạng thái và gửi cho tất cả các client WebSocket
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<Boolean> getLedStatus() {
        boolean status =webSocketHandler.getLedStatus();
        return ResponseEntity.ok(status);
    }

    @PostMapping("/update-time")
    public ResponseEntity setLedTime(@RequestBody Map<String, String> timeRequest) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        int hour = Integer.parseInt(timeRequest.get("hour"));
        int minute = Integer.parseInt(timeRequest.get("minute"));
        String timeString = hour + ":" + minute;
        int endHour = Integer.parseInt(timeRequest.get("endHour"));
        int endMinute = Integer.parseInt(timeRequest.get("endMinute"));
        String endTimeString = endHour + ":" + endMinute;
        webSocketHandler.setLedTime(timeString, endTimeString);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Led> getLedConfig(){
        Led led = new Led(webSocketHandler.getLedStatus(), webSocketHandler.getLedTime(), webSocketHandler.getLedEndTime(), webSocketHandler.isLedLightSensor());
        return ResponseEntity.ok(led);
    }

    @PostMapping("/update-light-sensor")
    public ResponseEntity updateLedLightSensor(@RequestBody boolean status) throws IOException {
        webSocketHandler.setLedLightSensor(status);
        return ResponseEntity.ok().build();
    }
}