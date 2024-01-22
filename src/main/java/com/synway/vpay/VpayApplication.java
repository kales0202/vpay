package com.synway.vpay;

import com.synway.vpay.bean.SettingVo;
import com.synway.vpay.repository.SettingRepository;
import com.synway.vpay.service.AdminService;
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
    @Resource
    private AdminService adminService;

    public static void main(String[] args) {
        SpringApplication.run(VpayApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        long count = settingRepository.count();
        if (count != 0) {
            return;
        }
        log.info("检测到系统为首次启动，正在进行数据库初始化...");
        SettingVo vo = new SettingVo();
        vo.setUser(VpayConstant.DEFAULT_USER);
        vo.setPass(VpayConstant.DEFAULT_PASSWORD);
        vo.setKey(VpayUtil.md5(new Date().toString()));
        vo.setJkState("-1");
        vo.setClose("5");
        vo.setPayQf("1");
        adminService.saveSetting(vo);
    }
}
