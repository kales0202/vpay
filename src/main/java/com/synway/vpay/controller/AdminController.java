package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.SettingVo;
import com.synway.vpay.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("/setting")
    public Result<SettingVo> setting() {
        return Result.success(adminService.getSetting());
    }
}
