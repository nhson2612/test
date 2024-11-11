package com.nhson.demo11;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public boolean getLedStatus() {
        return webSocketHandler.getLedStatus();
    }
}
