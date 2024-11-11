package com.nhson.demo11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SpringBootApplication
public class Demo11Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo11Application.class, args);
    }

    @PostMapping("/data")
    public ResponseEntity<String> receiveData(@RequestBody Map<String, Object> payload) {
        System.out.println("Received data: " + payload);
        return ResponseEntity.ok("Data received");
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }
}

