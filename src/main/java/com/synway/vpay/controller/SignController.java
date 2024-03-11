package com.synway.vpay.controller;

import com.synway.vpay.base.annotation.ValidSign;
import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.AppPushBO;
import com.synway.vpay.bean.MonitorListBO;
import com.synway.vpay.bean.MonitorVO;
import com.synway.vpay.bean.OrderCreateBO;
import com.synway.vpay.bean.OrderVO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Monitor;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.service.MonitorService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.spring.SignBoExecutor;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 需要验签的接口
 *
 * @since 0.1
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sign")
@ValidSign(SignBoExecutor.class) // 验签注解
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
     * 创建订单
     *
     * @param response HttpServletResponse
     * @param bo       订单创建入参
     * @return 生成的订单信息
     * @throws IOException IOException
     * @since 0.1
     */
    @PostMapping("/order/create")
    public Result<OrderVO> createOrder(HttpServletResponse response, @RequestBody OrderCreateBO bo) throws IOException {
        Order order = orderService.create(account, bo);
        if (bo.openHtml()) {
            response.sendRedirect("/pay?orderId=" + order.getOrderId());
        }
        return Result.success(new OrderVO(account, order));
    }

    /**
     * 关闭订单
     *
     * @param orderId 订单ID
     * @since 0.1
     */
    @GetMapping("/order/close")
    public Result<Void> closeOrder(String orderId) {
        orderService.closeOrder(account.getId(), orderId);
        return Result.success();
    }

    /**
     * 获取监控端信息
     *
     * @param monitorId 监控端ID
     * @since 0.1
     */
    @GetMapping("/monitor/info")
    public Result<List<MonitorVO>> getMonitorInfo(UUID monitorId) {
        MonitorListBO bo = new MonitorListBO();
        bo.setAccountId(account.getId());
        if (Objects.nonNull(monitorId)) {
            bo.setId(monitorId);
        }
        List<Monitor> monitors = monitorService.list(bo);
        return Result.success(monitors.stream().map(MonitorVO::from).toList());
    }

    /**
     * 监控端调用：监控端心跳检测
     *
     * @return void
     * @since 0.1
     */
    @GetMapping("/app/heartbeat")
    public Result<Void> heartbeat() {
        UUID monitorId = monitorService.getIdFromHeader();
        monitorService.updateMonitorState(account.getId(), monitorId, MonitorState.ONLINE, LocalDateTime.now());
        return Result.success(null, "成功");
    }

    /**
     * 监控端调用：接收监控端付款成功通知
     *
     * @param bo APP收款通知推送入参
     * @return Void
     * @since 0.1
     */
    @PostMapping("/app/push")
    public Result<Void> push(@RequestBody AppPushBO bo) {
        log.info("【{}】收款通知 --> type: {}, price: {}", account.getName(), bo.getType().getName(), bo.getPrice());

        // 先更新监控端状态
        UUID monitorId = monitorService.getIdFromHeader();
        monitorService.updateMonitorState(account.getId(), monitorId, MonitorState.ONLINE, LocalDateTime.now());

        // 检查是否重复推送
        LocalDateTime payTime = VpayUtil.toDatetime(request.getHeader(VpayConstant.HEADER_TIME));
        orderService.checkTimeIfPaid(payTime);

        // 查询未支付的订单
        Order order = orderService.findUnpaidOrder(bo.getPrice(), bo.getType());

        if (Objects.isNull(order)) {
            order = new Order();
            order.setAccountId(account.getId());
            order.setOrderId(VpayUtil.generateOrderId());
            order.setPayId("无订单转账" + order.getOrderId());
            order.setPayTime(payTime);
            order.setCloseTime(payTime);
            order.setPayType(bo.getType());
            order.setPrice(bo.getPrice());
            order.setReallyPrice(bo.getPrice());
            order.setState(OrderState.SUCCESS);
            orderService.save(order);
        } else {
            order.setPayType(bo.getType());
            order.setPayTime(payTime);
            orderService.sendNotify(order);
        }
        monitorService.updateLastPay(account.getId(), monitorId, payTime);
        return Result.success();
    }
}
