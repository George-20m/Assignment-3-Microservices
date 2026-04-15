package com.example.servicea.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ClientController {

    @GetMapping("/get-data")
    public String getData() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String response = restTemplate.getForObject("http://serviceb:8081/data", String.class);
            return "From Service A -> " + response;
        } catch (Exception e) {
            return "Service B is DOWN!";
        }
    }
}