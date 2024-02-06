package com.synway.vpay.service;

import com.synway.vpay.base.exception.IllegalOperationException;
import com.synway.vpay.entity.Account;
import com.synway.vpay.exception.AccountNotFoundException;
import com.synway.vpay.repository.AccountRepository;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
@CacheConfig(cacheNames = "account")
public class AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private Account account;

    /**
     * 保存设置
     *
     * @param param 设置
     * @since 0.1
     */
    @CacheEvict(cacheNames = "account")
    public Account save(Account param) {
        if (!Objects.equals(account.getName(), param.getName())) {
            throw new IllegalOperationException("不允许修改账户名");
        }
        Account db = accountRepository.findByName(account.getName());
        if (Objects.isNull(db)) {
            throw new AccountNotFoundException();
        }

        VpayUtil.copyProperties(param, db);
        db = accountRepository.save(db);
        account.copyFrom(db);
        return db;
    }

    /**
     * 删除设置
     *
     * @param account 设置
     * @since 0.1
     */
    @CacheEvict(cacheNames = "account")
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
    @CacheEvict(cacheNames = "account")
    public void delete(@NotNull UUID id) {
        if (Objects.equals(VpayConstant.SUPER_ID, id)) {
            throw new IllegalOperationException("不允许删除超级管理员！");
        }
        accountRepository.deleteById(id);
    }

    /**
     * 获取账户信息
     *
     * @param id 账户ID
     * @return Account
     * @since 0.1
     */
    @Cacheable(key = "#id")
    public Account findById(@NotNull UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(AccountNotFoundException::new);
    }

    /**
     * 获取账户信息
     *
     * @param name 账户名
     * @return Account
     * @since 0.1
     */
    @Cacheable(key = "#name")
    public Account findByName(@NotNull String name) {
        Account result = accountRepository.findByName(name);
        if (Objects.isNull(result)) {
            throw new AccountNotFoundException();
        }
        return result;
    }
}
