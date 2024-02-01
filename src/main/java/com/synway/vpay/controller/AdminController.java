package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.AccountState;
import com.synway.vpay.entity.Account;
import com.synway.vpay.service.AdminService;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private HttpSession session;

    @Resource
    private Account account;

    /**
     * 登录接口(测试)
     */
    @GetMapping("/login")
    public void login1(String name, String pass) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName(name);
        loginInfo.setPass(pass);
        this.login(loginInfo);
    }

    /**
     * 登录接口
     *
     * @param loginInfo 登录用户名和密码
     * @since 0.1
     */
    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginInfo loginInfo) {
        Account a = adminService.login(loginInfo.getName(), loginInfo.getPass());
        account.setId(a.getId());
        account.setName(a.getName());
        account.setPassword(a.getPassword());
        account.setKeyword(a.getKeyword());
        account.setWxPay(a.getWxPay());
        account.setAliPay(a.getAliPay());
        account.setPayQf(a.getPayQf());
        account.setClose(a.getClose());
        account.setSessionId(session.getId());
    }

    /**
     * 获取配置信息
     *
     * @return Account 配置信息，找不到则返回null
     * @since 0.1
     */
    @GetMapping("/account")
    public Result<Account> account() {
        return Result.success(VpayUtil.getTargetBean(account));
    }

    /**
     * 获取账户状态信息
     *
     * @return AccountState 监控端账户状态信息
     * @since 0.1
     */
    @GetMapping("/state")
    public Result<AccountState> accountState() {
        return Result.success(VpayUtil.getAccountState(account.getId().toString()));
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
        String name;

        /**
         * 密码
         *
         * @since 0.1
         */
        @NotNull
        String pass;
    }
}
