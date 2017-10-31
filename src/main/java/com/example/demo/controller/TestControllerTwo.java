package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endpoint/ON")
public class TestControllerTwo {

    @GetMapping
    public String firstEndpoint () {
        return "Timeout ON";
    }
}
