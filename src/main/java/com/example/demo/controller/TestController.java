package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping
    public String root () {
        return "root";
    }

    @GetMapping("notsecure")
    public String notSecureEndpoint () {
        return "Not secure";
    }
}
