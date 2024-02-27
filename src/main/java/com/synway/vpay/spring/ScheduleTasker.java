package com.synway.vpay.spring;

import com.synway.vpay.bean.AccountState;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.service.AccountService;
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
    private TempPriceService tempPriceService;

    @Scheduled(initialDelay = 5000, fixedDelay = 30000)
    public void checkAccountAndOrder() {
        log.info("====== 执行定时任务 ======");
        List<Account> accounts = accountService.findAll();
        for (Account account : accounts) {
            LocalDateTime now = LocalDateTime.now();
            int close = account.getClose();

            AccountState state = accountService.getAccountState(account.getId());
            if (state.getMonitorState() == MonitorState.ONLINE) {
                LocalDateTime expiredHeartTime = now.minus(3, ChronoUnit.MINUTES);
                if (state.getLastHeart().isBefore(expiredHeartTime)) {
                    log.warn("【{}】心跳超时，设置监控状态为离线", account.getName());
                    state.setMonitorState(MonitorState.OFFLINE);
                }
            } else {
                log.warn("【{}】监控状态为：{}", account.getName(), state.getMonitorState().getName());
            }

            // 查询过期订单
            List<Order> expiredOrders = orderService.findExpiredOrders(account.getId(), now.minus(close, ChronoUnit.MINUTES));

            // 删除缓存的实际支付金额
            for (Order order : expiredOrders) {
                tempPriceService.deleteByPayTypeAndPrice(account.getId(), order.getPayType(), order.getReallyPrice());
            }

            // 清理过期订单
            int rows = orderService.updateExpiredOrders(account.getId(), expiredOrders.stream().map(Order::getId).toList());
            log.info("【{}】成功清理{}个订单...", account.getName(), rows);
        }
    }
}
