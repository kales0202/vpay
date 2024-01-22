package com.synway.vpay.service;

import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.base.util.BaseUtil;
import com.synway.vpay.bean.SettingVo;
import com.synway.vpay.entity.Setting;
import com.synway.vpay.repository.SettingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminService {

    @Resource
    private SettingRepository settingRepository;

    public void saveSetting(SettingVo vo) {
        Map<String, Object> settingMap = BaseUtil.object2Map(vo);
        if (Objects.isNull(settingMap) || settingMap.isEmpty()) {
            return;
        }
        settingMap.entrySet().stream()
                .filter(e -> Objects.nonNull(e.getValue()))
                .forEach(e -> {
                    log.info("save setting: {}={}", e.getKey(), e.getValue());
                    Setting setting = new Setting(e.getKey(), String.valueOf(e.getValue()));
                    settingRepository.save(setting);
                });
    }

    public String getSettingByKey(String keyword) {
        Setting setting = settingRepository.findByKey(keyword);
        return Optional.ofNullable(setting).map(Setting::getContent).orElse("");
    }

    public SettingVo getSetting() {
        List<Setting> settings = settingRepository.findAll();
        Map<String, Object> settingMap = settings.stream().collect(Collectors.toMap(Setting::getKeyword, Setting::getContent));
        return BaseUtil.map2Object(settingMap, SettingVo.class);
    }

    public void login(String user, String pass) {
        if (Strings.isBlank(user) || Strings.isBlank(pass)) {
            throw new BusinessException("请输入账号和密码！");
        }

        String u = this.getSettingByKey("user");
        String p = this.getSettingByKey("pass");
        if (!Objects.equals(user, u) || !Objects.equals(pass, p)) {
            throw new BusinessException("账号或密码不正确！");
        }
    }
}
