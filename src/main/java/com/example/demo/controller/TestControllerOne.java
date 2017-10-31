package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endpoint/OFF")
public class TestControllerOne {

    @GetMapping
    public String firstEndpoint () {
        return "Timeout OFF";
    }
}
