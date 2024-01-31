package com.synway.vpay.service;

import com.synway.vpay.base.exception.IllegalOperationException;
import com.synway.vpay.base.exception.NotFoundException;
import com.synway.vpay.entity.Account;
import com.synway.vpay.repository.AccountRepository;
import com.synway.vpay.util.VpayConstant;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
public class AccountService {

    @Resource
    private AccountRepository accountRepository;

    /**
     * 保存设置
     *
     * @param account 设置
     * @since 0.1
     */
    public void save(Account account) {
        accountRepository.save(account);
    }

    /**
     * 删除设置
     *
     * @param account 设置
     * @since 0.1
     */
    public void delete(Account account) {
        if (Objects.equals(VpayConstant.SUPER_ID, account.getId())) {
            throw new IllegalOperationException("不允许删除超级管理员！");
        }
        accountRepository.delete(account);
    }

    /**
     * 删除设置 by id
     *
     * @param id 设置ID
     * @since 0.1
     */
    public void delete(@NotNull UUID id) {
        if (Objects.equals(VpayConstant.SUPER_ID, id)) {
            throw new IllegalOperationException("不允许删除超级管理员！");
        }
        accountRepository.deleteById(id);
    }

    /**
     * 获取用户信息
     *
     * @param id 用户名
     * @return Account
     * @since 0.1
     */
    public Account findById(@NotNull UUID id) {
        return accountRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    /**
     * 获取用户信息
     *
     * @param name 用户名
     * @return Account
     * @since 0.1
     */
    public Account findByName(@NotNull String name) {
        return accountRepository.findByName(name);
    }
}
