package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class InitController {
    @GetMapping("/")
    public ResponseEntity<Object> testApp(){
        return ResponseEntity.ok().body("App is running now");
    }
}
