package com.nhson.demo11;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/led")
public class LedController {

    @Autowired
    private LedWebSocketHandler webSocketHandler;

    private static Map<String,String> ledConfig = new HashMap<>();

    @PostMapping("/update")
    public void updateLedStatus(@RequestBody boolean status) throws IOException {
        webSocketHandler.setLedStatus(status); // Cập nhật trạng thái và gửi cho tất cả các client WebSocket
    }

    @GetMapping("/status")
    public boolean getLedStatus() {
        return webSocketHandler.getLedStatus();
    }

    @PostMapping("/config")
    public void updateLedConfig(@RequestBody Map<String, String> config) {
        ledConfig.putAll(config);
    }
    @GetMapping("/config")
    public Map<String, String> getLedConfig() {
        return ledConfig;
    }
}
