package com.synway.vpay.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @RequestMapping("hello")
    public String hello(boolean error) {
        if (error) {
            throw new RuntimeException("error occur");
        }
        return "hello";
    }
}
