package com.synway.vpay.controller;


import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.AccountState;
import com.synway.vpay.bean.OrderCreateBO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 验签通过后可访问的接口
 *
 * @since 0.1
 */
@Validated
@Controller
@RequestMapping("/public")
public class PublicController {

    @Resource
    private OrderService orderService;

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
    @ResponseBody
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
     * 查询订单信息
     *
     * @param orderId 订单ID
     * @return 生成的订单信息
     * @since 0.1
     */
    @GetMapping("/order/get")
    @ResponseBody
    public Result<Order> getOrder(String orderId) {
        return Result.success(orderService.findByOrderId(orderId));
    }

    /**
     * 查询订单状态（仅超级管理员）
     *
     * @param orderId 订单ID
     * @return 订单支付完成后的跳转地址（带回调参数）
     * @since 0.1
     */
    @GetMapping("/order/check")
    @ResponseBody
    public Result<String> checkOrder(String orderId) {
        return Result.success(orderService.checkOrder(orderId));
    }

    /**
     * 关闭订单
     *
     * @param orderId 订单ID
     * @since 0.1
     */
    @GetMapping("/order/close")
    @ResponseBody
    public Result<Void> closeOrder(String orderId) {
        orderService.closeOrder(orderId);
        return Result.success();
    }

    /**
     * 查询服务端状态（如果不传入账户名name参数，则默认获取超级管理员的状态）
     *
     * @since 0.1
     */
    @GetMapping("/account/state")
    @ResponseBody
    public Result<AccountState> getAccountState() {
        return Result.success(VpayUtil.getAccountState(account.getId()));
    }
}
