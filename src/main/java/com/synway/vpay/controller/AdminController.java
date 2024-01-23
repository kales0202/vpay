package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.SettingVo;
import com.synway.vpay.service.AdminService;
import com.synway.vpay.util.VpayConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private HttpSession session;

    @PostMapping("/login")
    public void login(@RequestBody LoginVo loginVo) {
        adminService.login(loginVo.getUser(), loginVo.getPass());
        session.setAttribute(VpayConstant.USER, loginVo.getUser());
    }

    @GetMapping("/setting")
    public Result<SettingVo> setting() {
        return Result.success(adminService.getSetting());
    }

    @Data
    private static class LoginVo {
        String user;
        String pass;
    }
}
