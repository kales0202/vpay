package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.entity.Account;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 管理员相关接口
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @Resource
    private Account account;

    /**
     * 登录接口
     *
     * @param loginBO 登录账户名和密码
     * @return nothing
     * @since 0.1
     */
    @PostMapping("/login")
    public Result<Void> login(@RequestBody @Valid LoginBO loginBO) {
        Account db = accountService.login(loginBO.getName(), loginBO.getPass());
        account.copyFrom(db);
        return Result.success();
    }

    /**
     * 修改密码
     *
     * @param bo 修改密码BO
     * @since 0.1
     */
    @PostMapping("/password")
    public Result<Void> password(@RequestBody @Valid PasswordBO bo) {
        String newPassword = accountService.changePassword(bo);
        account.setPassword(newPassword);
        return Result.success();
    }

    /**
     * 获取配置信息
     *
     * @return Account 配置信息，找不到则返回null
     * @since 0.1
     */
    @GetMapping
    public Result<Account> get() {
        return Result.success(VpayUtil.getTargetBean(account));
    }

    /**
     * 保存配置信息
     *
     * @return Account 配置信息，找不到则返回null
     * @since 0.1
     */
    @PostMapping
    public Result<Account> save(@RequestBody @Valid Account param) {
        Account db = accountService.save(param);
        account.copyFrom(db);
        return Result.success(db);
    }

    /**
     * 登录账户名和密码
     *
     * @since 0.1
     */
    @Data
    public static class LoginBO {

        /**
         * 账户名
         *
         * @since 0.1
         */
        @NotNull
        private String name;

        /**
         * 密码
         *
         * @since 0.1
         */
        @NotNull
        private String pass;
    }

    /**
     * 修改密码BO
     *
     * @since 0.1
     */
    @Data
    public static class PasswordBO {

        /**
         * 账户ID
         *
         * @since 0.1
         */
        private UUID accountId;

        /**
         * 旧密码
         *
         * @since 0.1
         */
        @NotNull
        private String oldPassword;

        /**
         * 新密码
         *
         * @since 0.1
         */
        @NotNull
        private String newPassword;
    }

}
