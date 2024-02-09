package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.AccountState;
import com.synway.vpay.bean.OrderCreateBO;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * 需要验签的接口
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping("/sign")
public class SignController {

    @Resource
    private OrderService orderService;

    @Resource
    private AccountService accountService;

    @Resource
    private HttpServletRequest request;

    /**
     * 创建订单
     *
     * @param response HttpServletResponse
     * @param bo       订单创建入参
     * @return 生成的订单信息
     * @throws IOException IOException
     * @since 0.1
     */
    @PostMapping("/order/create")
    public Result<Order> createOrder(HttpServletResponse response, @RequestBody OrderCreateBO bo) throws IOException {
        if (Strings.isBlank(bo.getAccountName())) {
            bo.setAccountName(VpayConstant.SUPER_ACCOUNT);
        }
        Order order = orderService.create(bo);
        if (bo.openHtml()) {
            response.sendRedirect("/pay?orderId=" + order.getOrderId());
        }
        return Result.success(order);
    }

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
     * 查询服务端状态
     *
     * @since 0.1
     */
    @GetMapping("/account/state")
    public Result<AccountState> getAccountState() {
        return Result.success(accountService.getAccountState());
    }

    /**
     * 监控端调用：监控端心跳检测
     *
     * @return void
     * @since 0.1
     */
    @GetMapping("/heartbeat")
    public Result<Void> heartbeat() {
        // 更新监控端状态
        accountService.updateMonitorState(MonitorState.ONLINE, LocalDateTime.now());
        return Result.success();
    }

    /**
     * 监控端调用：接收监控端付款成功通知
     *
     * @param payType 支付方式
     * @param price   支付金额
     * @return Void
     * @since 0.1
     */
    @GetMapping("/push")
    public Result<Void> push(PayType payType, BigDecimal price) {
        LocalDateTime payTime = VpayUtil.toDatetime(request.getHeader("vpay-time"));

        // 检查是否重复推送
        orderService.checkTimeIfPaid(payTime);

        // 查询未支付的订单
        Order order = orderService.findUnpaidOrder(price, payType);

        if (Objects.isNull(order)) {
            order = new Order();
            order.setPayId("无订单转账" + UUID.randomUUID());
            order.setOrderId(VpayUtil.generateOrderId());
            order.setPayTime(payTime);
            order.setCloseTime(payTime);
            order.setPayType(payType);
            order.setPrice(price);
            order.setReallyPrice(price);
            order.setState(OrderState.SUCCESS);
            orderService.save(order);
        } else {
            order.setPayTime(payTime);
            orderService.sendNotify(order);
        }
        accountService.updateLastPay(payTime);
        return Result.success();
    }
}
