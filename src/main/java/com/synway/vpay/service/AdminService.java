package com.synway.vpay.service;

import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.entity.Setting;
import com.synway.vpay.repository.SettingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AdminService {

    @Resource
    private SettingRepository settingRepository;

    public Setting login(String user, String pass) {
        if (Strings.isBlank(user) || Strings.isBlank(pass)) {
            throw new BusinessException("请输入账号和密码！");
        }

        Setting setting = settingRepository.findByUsername(user);
        if (Objects.isNull(setting)) {
            throw new BusinessException("账号不存在！");
        }
        if (!Objects.equals(user, setting.getUsername()) || !Objects.equals(pass, setting.getPassword())) {
            throw new BusinessException("账号或密码不正确！");
        }
        return setting;
    }
}
