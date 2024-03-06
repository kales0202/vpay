package com.synway.vpay.spring;

import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Monitor;
import com.synway.vpay.entity.PayCode;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.repository.AccountRepository;
import com.synway.vpay.repository.MonitorRepository;
import com.synway.vpay.repository.OrderRepository;
import com.synway.vpay.repository.PayCodeRepository;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.service.MonitorService;
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
import java.util.List;
import java.util.UUID;

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
    private AccountService accountService;

    @Resource
    private MonitorService monitorService;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private MonitorRepository monitorRepository;

    @Resource
    private PayCodeRepository payCodeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (accountRepository.count() != 0) {
            return;
        }
        log.info("检测到系统为首次启动，初始化数据库中...");

        // initAccount();
        // TODO... 测试数据
        initAccountFake();

        initMonitors();
        log.info("初始化数据库完成...");
    }

    private void initAccount() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setName(VpayConstant.SUPER_ACCOUNT);
        account.setPassword(VpayUtil.jbEncrypt(VpayConstant.SUPER_ACCOUNT));
        account.setKeyword(VpayUtil.md5(LocalDateTime.now().toString()));
        account = accountRepository.save(account);

        Monitor monitor = new Monitor();
        monitor.setAccountId(account.getId());
        monitor.setName(VpayConstant.DEFAULT_MONITOR_NAME);
        monitor = monitorRepository.saveAndFlush(monitor);
    }

    private void initAccountFake() {
        log.debug("初始化模拟数据中...");

        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setName(VpayConstant.SUPER_ACCOUNT);
        account.setPassword(VpayUtil.jbEncrypt(VpayConstant.SUPER_ACCOUNT));
        account.setKeyword("f3ba2ab83fc2465dd567e70129772d3a");
        account.setNotifyUrl("http://localhost:8080/fakeNotifyUrl");
        account.setReturnUrl("http://localhost:8080/fakeReturnUrl");
        accountRepository.saveAndFlush(account);

        Monitor monitor = new Monitor();
        monitor.setAccountId(account.getId());
        monitor.setName(VpayConstant.DEFAULT_MONITOR_NAME);
        monitor = monitorRepository.saveAndFlush(monitor);

        // 模拟数据
        com.synway.vpay.entity.Order order = new com.synway.vpay.entity.Order();
        order.setOrderId("202402071918485874");
        order.setAccountId(account.getId());
        order.setPayId("191f06f2-8c84-481e-8e9c-3f6beb9c1551");
        order.setPrice(new BigDecimal("2.11"));
        order.setReallyPrice(new BigDecimal("2.12"));
        order.setState(OrderState.WAIT);
        order.setPayType(PayType.WECHAT);
        order.setPayUrl("wxp://djaskjdlasjkldjklasjdkljaskldjklasjdklasd");
        orderRepository.saveAndFlush(order);

        // 模拟数据
        PayCode payCode = new PayCode();
        payCode.setId(UUID.randomUUID());
        payCode.setAccountId(account.getId());
        payCode.setName("测试");
        payCode.setMonitor(monitor);
        payCode.setType(PayType.WECHAT);
        payCode.setPayment("wxp://djaskjdlasjkldjklasjdkljaskldjklasjdklasd");
        payCode = payCodeRepository.saveAndFlush(payCode);
    }

    void initMonitors() {
        log.info("缓存监控端信息...");
        List<Account> accounts = accountService.findAll();
        for (Account account : accounts) {
            monitorService.listAll(account.getId());
            monitorService.resetStrategyCache(account.getId(), account.getStrategy());
        }
    }
}
