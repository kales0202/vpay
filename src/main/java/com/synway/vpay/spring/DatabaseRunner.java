package com.synway.vpay.spring;

import com.synway.vpay.entity.Account;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.repository.AccountRepository;
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
    private OrderRepository orderRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (accountRepository.count() != 0) {
            return;
        }
        log.info("检测到系统为首次启动，初始化数据库中...");
        initAccount();
        // TODO... 先搞点模拟数据
        generateSomeFakeData();
        log.info("初始化数据库完成...");
    }

    private void initAccount() {
        Account account = new Account();
        account.setId(VpayConstant.SUPER_ID);
        account.setName(VpayConstant.SUPER_ACCOUNT);
        account.setPassword(VpayConstant.SUPER_ACCOUNT);
        // TODO... 暂时使用固定密钥
        // account.setKeyword(VpayUtil.md5(new Date().toString()));
        account.setKeyword("f3ba2ab83fc2465dd567e70129772d3a");
        account.setWxPay("");
        account.setAliPay("");
        accountRepository.save(account);
    }

    // 生成一些假数据
    private void generateSomeFakeData() {
        com.synway.vpay.entity.Order order = new com.synway.vpay.entity.Order();
        order.setPrice(new BigDecimal("2.11"));
        order.setState(OrderState.SUCCESS);
        order.setPayType(PayType.WECHAT);
        orderRepository.save(order);

        VpayUtil.updateLastHeart(VpayConstant.SUPER_ID.toString(), LocalDateTime.now());
    }
}
