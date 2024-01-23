package com.synway.vpay.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class ResourceController {

    @GetMapping()
    public String index() {
        return "/index.html";
    }

    @GetMapping({"login"})
    public String notFound() {
        return "/login.html";
    }

    @GetMapping({"favicon.ico", "favicon"})
    @ResponseBody
    public void returnNoFavicon() {
    }
}
