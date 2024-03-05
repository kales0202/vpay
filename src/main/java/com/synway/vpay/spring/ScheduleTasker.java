package com.synway.vpay.spring;

import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Monitor;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.service.MonitorService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.service.TempPriceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class ScheduleTasker {

    @Resource
    private OrderService orderService;

    @Resource
    private AccountService accountService;

    @Resource
    private MonitorService monitorService;

    @Resource
    private TempPriceService tempPriceService;

    @Scheduled(initialDelay = 5000, fixedDelay = 30000)
    public void checkAccountAndOrder() {
        log.info("====== 执行定时任务 ======");
        List<Account> accounts = accountService.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Account account : accounts) {

            // 检查监控端状态
            List<Monitor> monitors = monitorService.getMonitors(account.getId());
            for (Monitor monitor : monitors) {
                if (monitor.getState() == MonitorState.ONLINE) {
                    LocalDateTime expiredHeartTime = now.minus(3, ChronoUnit.MINUTES);
                    if (monitor.getLastHeart().isBefore(expiredHeartTime)) {
                        log.warn("【{}】[{}]心跳超时，设置监控状态为离线", account.getName(), monitor.getName());
                        monitor.setState(MonitorState.OFFLINE);
                    }
                } else {
                    log.warn("【{}】[{}]监控状态为：{}", account.getName(), monitor.getName(), monitor.getState().getName());
                }
            }

            LocalDateTime deadline = now.minus(account.getClose(), ChronoUnit.MINUTES);
            // 清理过期订单
            int rows = orderService.updateExpiredOrders(account.getId(), deadline);
            // 清理过期缓存金额
            int temp = tempPriceService.deleteExpiredPrices(account.getId(), deadline);

            log.info("【{}】成功清理{}个订单...", account.getName(), rows);
            log.debug("【{}】成功清理{}个缓存金额...", account.getName(), temp);
        }
    }
}
