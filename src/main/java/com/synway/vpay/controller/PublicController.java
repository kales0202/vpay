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
     * @param bo 订单创建入参信息
     * @since 0.1
     */
    @PostMapping("/order/create")
    @ResponseBody
    public Result<Order> createOrder(HttpServletResponse response, @RequestBody OrderCreateBO bo) throws IOException {
        if (Strings.isBlank(bo.getAccountName())) {
            bo.setAccountName(VpayConstant.SUPER_ACCOUNT);
        }
        Order order = orderService.save(bo);
        if (bo.openHtml()) {
            response.sendRedirect("/pay?order=" + order.getOrderId());
        }
        return Result.success(order);
    }

    /**
     * 查询服务端状态（如果不传入账户名name参数，则默认获取超级管理员的状态）
     *
     * @since 0.1
     */
    @PostMapping("/account/state")
    @ResponseBody
    public Result<AccountState> getAccountState() {
        return Result.success(VpayUtil.getAccountState(account.getId().toString()));
    }
}
