package com.synway.vpay.service;

import com.synway.vpay.base.exception.IllegalOperationException;
import com.synway.vpay.base.exception.NotFoundException;
import com.synway.vpay.entity.Setting;
import com.synway.vpay.repository.SettingRepository;
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
public class SettingService {

    @Resource
    private SettingRepository settingRepository;

    /**
     * 保存设置
     *
     * @param setting 设置
     * @since 0.1
     */
    public void save(Setting setting) {
        settingRepository.save(setting);
    }

    /**
     * 删除设置
     *
     * @param setting 设置
     * @since 0.1
     */
    public void delete(Setting setting) {
        if (Objects.equals(VpayConstant.SUPER_ID, setting.getId())) {
            throw new IllegalOperationException("不允许删除超级管理员！");
        }
        settingRepository.delete(setting);
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
        settingRepository.deleteById(id);
    }

    /**
     * 获取用户信息
     *
     * @param id 用户名
     * @return Setting
     * @since 0.1
     */
    public Setting findById(@NotNull UUID id) {
        return settingRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return Setting
     * @since 0.1
     */
    public Setting findByUsername(@NotNull String username) {
        return settingRepository.findByUsername(username);
    }
}
