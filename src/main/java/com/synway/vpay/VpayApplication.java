package com.synway.vpay;

import com.google.common.collect.Lists;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Menu;
import com.synway.vpay.repository.AccountRepository;
import com.synway.vpay.repository.MenuRepository;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@EnableCaching
@SpringBootApplication
public class VpayApplication implements ApplicationRunner {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private MenuRepository menuRepository;

    public static void main(String[] args) {
        SpringApplication.run(VpayApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (accountRepository.count() != 0) {
            return;
        }
        log.info("检测到系统为首次启动，正在进行数据库初始化...");
        initAccount();
        initMenu();
        log.info("数据库初始化完成...");
    }

    private void initAccount() {
        Account account = new Account();
        account.setId(VpayConstant.SUPER_ID);
        account.setName(VpayConstant.SUPER_ACCOUNT);
        account.setPassword(VpayConstant.SUPER_ACCOUNT);
        account.setKeyword(VpayUtil.md5(new Date().toString()));
        accountRepository.save(account);
    }

    private void initMenu() {
        List<Menu> menus = new ArrayList<>();
        menus.add(new Menu("系统设置", "admin/account.html"));
        menus.add(new Menu("监控端设置", "admin/jk.html"));

        // 微信二维码
        List<Menu> subWXMenus = Lists.newArrayList(
                new Menu("添加", "admin/addwxqrcode.html"),
                new Menu("管理", "admin/wxqrcodelist.html")
        );
        menus.addAll(subWXMenus);
        menus.add(new Menu("微信二维码", subWXMenus));

        // 支付宝二维码
        List<Menu> subZFBMenus = Lists.newArrayList(
                new Menu("添加", "admin/addzfbqrcode.html"),
                new Menu("管理", "admin/zfbqrcodelist.html")
        );
        menus.addAll(subZFBMenus);
        menus.add(new Menu("支付宝二维码", subZFBMenus));

        menus.add(new Menu("订单列表", "admin/orderlist.html"));
        menus.add(new Menu("API说明", "../api.html"));

        menuRepository.saveAll(menus);
    }
}
