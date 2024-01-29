package com.synway.vpay.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 静态页面
 */
@Controller
@RequestMapping("/")
public class ResourceController {

    /**
     * 首页
     *
     * @return 首页
     */
    @GetMapping()
    public String index() {
        return "/index.html";
    }

    /**
     * 登录页
     *
     * @return 登录页
     */
    @GetMapping({"login"})
    public String notFound() {
        return "/login.html";
    }

    /**
     * favicon.ico
     */
    @GetMapping({"favicon.ico", "favicon"})
    @ResponseBody
    public void returnNoFavicon() {
    }
}
