package com.synway.vpay.spring;

import com.google.common.collect.Lists;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Menu;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.repository.AccountRepository;
import com.synway.vpay.repository.MenuRepository;
import com.synway.vpay.repository.OrderRepository;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 初始化数据库
 *
 * @since 0.1
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseRunner implements ApplicationRunner {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private MenuRepository menuRepository;

    @Resource
    private OrderRepository orderRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (accountRepository.count() != 0) {
            return;
        }
        log.info("检测到系统为首次启动，初始化数据库中...");
        initAccount();
        initMenu();
        // TODO... 先搞点假数据
        generateSomeFakeData();
        log.info("初始化数据库完成...");
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
        menus.add(new Menu("系统设置", "admin/setting.html"));
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

    // 生成一些假数据
    private void generateSomeFakeData() {
        com.synway.vpay.entity.Order order = new com.synway.vpay.entity.Order();
        order.setPrice(new BigDecimal("2.11"));
        order.setState(OrderState.SUCCESS);
        order.setType(PayType.WECHAT);
        orderRepository.save(order);

        VpayUtil.updateLastHeart(VpayConstant.SUPER_ID.toString(), LocalDateTime.now());
    }
}
