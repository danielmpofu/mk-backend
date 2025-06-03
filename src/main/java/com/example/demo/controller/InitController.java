package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/init")
public class InitController {
    @GetMapping("/")
    public ResponseEntity<Object> testApp() {
        return ResponseEntity.ok().body("App is running now");
    }

    @GetMapping("/tomcat")
    public String home() {
        return "Hello, Tomcat is working!";
    }

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @GetMapping("/endpoints")
    public ResponseEntity<List<String>> listEndpoints() {
        try {
            List<String> endpoints = requestMappingHandlerMapping.getHandlerMethods()
                    .keySet()
                    .stream()
                    .map(mapping -> {
                        String methods = mapping.getMethodsCondition().getMethods().toString();
                        String patterns = mapping.getPatternsCondition().getPatterns().toString();
                        return methods + " " + patterns;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(endpoints);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
