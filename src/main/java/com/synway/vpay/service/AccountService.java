package com.synway.vpay.service;

import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.base.exception.IllegalArgumentException;
import com.synway.vpay.base.exception.IllegalOperationException;
import com.synway.vpay.controller.AccountController.PasswordBO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.exception.AccountNotFoundException;
import com.synway.vpay.repository.AccountRepository;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
@CacheConfig(cacheNames = "account")
public class AccountService {

    @Resource
    private AccountRepository accountRepository;

    public Account login(String name, String pass) {
        if (Strings.isBlank(name) || Strings.isBlank(pass)) {
            throw new IllegalArgumentException("请输入账号和密码！");
        }

        Account db = accountRepository.findByName(name);
        if (Objects.isNull(db) || !VpayUtil.jbVerify(pass, db.getPassword())) {
            throw new BusinessException("账号或密码不正确！");
        }
        return db;
    }

    @CacheEvict(cacheNames = "account")
    public String changePassword(PasswordBO bo) {
        if (Strings.isBlank(bo.getOldPassword()) || Strings.isBlank(bo.getNewPassword())) {
            throw new IllegalArgumentException("请输入原密码和新密码！");
        }

        Account db = accountRepository.findById(bo.getAccountId())
                .orElseThrow(AccountNotFoundException::new);
        if (!VpayUtil.jbVerify(bo.getOldPassword(), db.getPassword())) {
            throw new IllegalOperationException("原密码输入错误！");
        }

        db.setPassword(VpayUtil.jbEncrypt(bo.getNewPassword()));
        accountRepository.save(db);

        return db.getPassword();
    }

    /**
     * 保存设置
     *
     * @param param 设置
     * @since 0.1
     */
    @CacheEvict(cacheNames = "account")
    public Account save(Account param) {
        Account db = accountRepository.findById(param.getId())
                .orElseThrow(AccountNotFoundException::new);
        if (!Objects.equals(db.getName(), param.getName())) {
            throw new IllegalOperationException("不允许修改账户名");
        }

        VpayUtil.copyNonNullProperties(param, db);
        db = accountRepository.save(db);
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
        if (Objects.equals(VpayConstant.SUPER_ACCOUNT, account.getName())) {
            throw new IllegalOperationException("不允许删除超级管理员！");
        }
        accountRepository.delete(account);
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

    /**
     * 获取全量账户信息
     *
     * @return accounts
     * @since 0.1
     */
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
