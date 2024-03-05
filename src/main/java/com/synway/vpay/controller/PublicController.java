package com.synway.vpay.controller;


import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.OrderCreateBO;
import com.synway.vpay.bean.OrderVO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.util.VpayConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
 * 可公开访问的接口
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
    private AccountService accountService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private Account account;

    /**
     * 创建订单（方法内进行验签）
     *
     * @param response HttpServletResponse
     * @param bo       订单创建入参
     * @return 生成的订单信息
     * @throws IOException IOException
     * @since 0.1
     */
    @PostMapping("/order/create")
    public Result<OrderVO> createOrder(HttpServletResponse response, @RequestBody OrderCreateBO bo) throws IOException {
        this.simulatedLogin();
        Order order = orderService.create(bo);
        if (bo.openHtml()) {
            response.sendRedirect("/pay?orderId=" + order.getOrderId());
        }
        return Result.success(new OrderVO(account, order));
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
    public Result<OrderVO> getOrder(String orderId) {
        this.simulatedLogin();
        Order order = orderService.findByOrderId(orderId);
        return Result.success(new OrderVO(account, order));
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
        this.simulatedLogin();
        return Result.success(orderService.checkOrder(orderId));
    }

    private void simulatedLogin() {
        String accountName = request.getHeader("Vpay-Account");
        if (Strings.isBlank(accountName)) {
            accountName = VpayConstant.SUPER_ACCOUNT;
        }
        Account db = accountService.findByName(accountName);
        account.copyFrom(db);
    }
}
