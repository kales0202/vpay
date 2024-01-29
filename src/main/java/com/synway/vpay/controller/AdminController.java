package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.entity.Menu;
import com.synway.vpay.entity.Setting;
import com.synway.vpay.service.AdminService;
import com.synway.vpay.service.SettingService;
import com.synway.vpay.util.VpayConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理员相关接口
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private SettingService settingService;

    @Resource
    private HttpSession session;

    /**
     * 登录接口
     *
     * @param loginInfo 登录用户名和密码
     * @since 0.1
     */
    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginInfo loginInfo) {
        Setting setting = adminService.login(loginInfo.getUser(), loginInfo.getPass());
        session.setAttribute(VpayConstant.USER, setting);
    }

    /**
     * 根据用户名获取配置信息
     *
     * @param username 用户名
     * @return Setting 配置信息，找不到则返回null
     * @since 0.1
     */
    @GetMapping("/setting/{username}")
    public Result<Setting> setting(@PathVariable String username) {
        return Result.success(settingService.findByUsername(username));
    }

    /**
     * 获取菜单
     *
     * @return 菜单集合
     * @since 0.1
     */
    @RequestMapping("/getMenu")
    public List<Menu> getMenu() {
        return adminService.getMenu();
    }

    /**
     * 登录用户名和密码
     *
     * @since 0.1
     */
    @Data
    private static class LoginInfo {

        /**
         * 用户名
         *
         * @since 0.1
         */
        @NotNull
        String user;

        /**
         * 密码
         *
         * @since 0.1
         */
        @NotNull
        String pass;
    }
}
