package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.service.MonitorService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 需要验签的接口
 *
 * @since 0.1
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sign")
public class SignController {

    @Resource
    private OrderService orderService;

    @Resource
    private MonitorService monitorService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private Account account;

    /**
     * 关闭订单
     *
     * @param orderId 订单ID
     * @since 0.1
     */
    @GetMapping("/order/close")
    public Result<Void> closeOrder(String orderId) {
        orderService.closeOrder(orderId);
        return Result.success();
    }

    /**
     * 监控端调用：监控端心跳检测
     *
     * @return void
     * @since 0.1
     */
    @GetMapping("/app/heartbeat")
    public Result<Void> heartbeat() {
        String monitorName = monitorService.getNameFromHeader();
        monitorService.updateMonitorState(account.getId(), monitorName, MonitorState.ONLINE, LocalDateTime.now());
        return Result.success(null, "成功");
    }

    /**
     * 监控端调用：接收监控端付款成功通知
     *
     * @param type  支付方式
     * @param price 支付金额
     * @return Void
     * @since 0.1
     */
    @GetMapping("/app/push")
    public Result<Void> push(@RequestParam PayType type, @RequestParam BigDecimal price) {
        log.info("【{}】收款通知 --> type: {}, price: {}", account.getName(), type.getName(), price);

        // 先更新监控端状态
        String monitorName = monitorService.getNameFromHeader();
        monitorService.updateMonitorState(account.getId(), monitorName, MonitorState.ONLINE, LocalDateTime.now());

        // 检查是否重复推送
        LocalDateTime payTime = VpayUtil.toDatetime(request.getHeader("Vpay-Time"));
        orderService.checkTimeIfPaid(payTime);

        // 查询未支付的订单
        Order order = orderService.findUnpaidOrder(price, type);

        if (Objects.isNull(order)) {
            order = new Order();
            order.setAccountId(account.getId());
            order.setOrderId(VpayUtil.generateOrderId());
            order.setPayId("无订单转账" + order.getOrderId());
            order.setPayTime(payTime);
            order.setCloseTime(payTime);
            order.setPayType(type);
            order.setPrice(price);
            order.setReallyPrice(price);
            order.setState(OrderState.SUCCESS);
            orderService.save(order);
        } else {
            order.setPayType(type);
            order.setPayTime(payTime);
            orderService.sendNotify(order);
        }
        monitorService.updateLastPay(account.getId(), monitorName, payTime);
        return Result.success();
    }
}
