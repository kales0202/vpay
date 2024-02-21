package com.synway.vpay.service;

import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.base.exception.IllegalOperationException;
import com.synway.vpay.bean.AccountState;
import com.synway.vpay.entity.Account;
import com.synway.vpay.enums.MonitorState;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
@CacheConfig(cacheNames = "account")
public class AccountService {

    /**
     * 账户当前状态
     *
     * @since 0.1
     */
    private static final Map<UUID, AccountState> ACCOUNT_STATE_MAP = new HashMap<>();

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private Account account;

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
    public void changePassword(String oldPassword, String newPassword) {
        if (Strings.isBlank(oldPassword) || Strings.isBlank(newPassword)) {
            throw new IllegalArgumentException("请输入原密码和新密码！");
        }

        Account db = accountRepository.findByName(account.getName());
        if (!VpayUtil.jbVerify(oldPassword, db.getPassword())) {
            throw new IllegalOperationException("原密码输入错误！");
        }
        db.setPassword(VpayUtil.jbEncrypt(newPassword));
        accountRepository.save(db);

        account.setPassword(db.getPassword());
    }

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

        VpayUtil.copyNonNullProperties(param, db);
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

    /**
     * 获取全量账户信息
     *
     * @return accounts
     * @since 0.1
     */
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    /**
     * 获取账户状态
     *
     * @return 账户状态信息
     * @since 0.1
     */
    public AccountState getAccountState() {
        return this.getAccountState(account.getId());
    }

    /**
     * 获取账户状态
     *
     * @param id 账户ID
     * @return 账户状态信息
     * @since 0.1
     */
    public AccountState getAccountState(UUID id) {
        AccountState accountState;
        if (ACCOUNT_STATE_MAP.containsKey(id)) {
            accountState = ACCOUNT_STATE_MAP.get(id);
        } else {
            Account ac = this.findById(id);
            accountState = new AccountState(id, ac.getName());
            ACCOUNT_STATE_MAP.put(id, accountState);
        }
        return accountState;
    }

    /**
     * 更新账户信息：最后支付时间
     *
     * @param lastPay 最后支付时间
     * @since 0.1
     */
    public void updateLastPay(LocalDateTime lastPay) {
        AccountState accountState = this.getAccountState();
        accountState.setLastPay(lastPay);
    }

    /**
     * 更新账户信息：监控端状态
     *
     * @param monitorState 监控端状态
     * @since 0.1
     */
    public void updateMonitorState(MonitorState monitorState, LocalDateTime lastHeart) {
        AccountState accountState = this.getAccountState();
        accountState.setMonitorState(monitorState);
        accountState.setLastHeart(lastHeart);
    }
}
