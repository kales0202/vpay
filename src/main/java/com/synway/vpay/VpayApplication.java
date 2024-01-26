package com.synway.vpay;

import com.synway.vpay.entity.Setting;
import com.synway.vpay.repository.SettingRepository;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@Slf4j
@SpringBootApplication
public class VpayApplication implements ApplicationRunner {

    @Resource
    private SettingRepository settingRepository;

    public static void main(String[] args) {
        SpringApplication.run(VpayApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (settingRepository.count() != 0) {
            return;
        }
        log.info("检测到系统为首次启动，正在进行数据库初始化...");
        Setting setting = new Setting();
        setting.setId(VpayConstant.SUPER_ID);
        setting.setUsername(VpayConstant.SUPER_USER);
        setting.setPassword(VpayConstant.SUPER_USER);
        setting.setKeyword(VpayUtil.md5(new Date().toString()));
        settingRepository.save(setting);
    }
}
