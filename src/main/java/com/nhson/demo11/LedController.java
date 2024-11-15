package com.nhson.demo11;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void updateLedStatus(@RequestBody boolean status) throws IOException {
        webSocketHandler.setLedStatus(status); // Cập nhật trạng thái và gửi cho tất cả các client WebSocket
    }

    @GetMapping("/status")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public boolean getLedStatus() {
        return webSocketHandler.getLedStatus();
    }

    @PostMapping("/update-time")
    public void setLedTime(@RequestBody Map<String, String> timeRequest) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        int hour = Integer.parseInt(timeRequest.get("hour"));
        int minute = Integer.parseInt(timeRequest.get("minute"));
        LocalTime requestedTime = LocalTime.of(hour, minute);

        LocalDateTime targetDateTime;

        if (now.toLocalTime().isBefore(requestedTime)) {
            targetDateTime = LocalDateTime.of(now.toLocalDate(), requestedTime);
        } else {
            targetDateTime = LocalDateTime.of(now.toLocalDate().plusDays(1), requestedTime);
        }
        Date targetDate = java.sql.Timestamp.valueOf(targetDateTime);
        webSocketHandler.setLedTime(targetDate);
    }

    @GetMapping
    public Led getLedConfig(){
        return new Led(webSocketHandler.getLedStatus(), webSocketHandler.getLedTime());
    }
}