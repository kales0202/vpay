package com.synway.vpay.controller;


import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.OrderVO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.util.VpayConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        String name = request.getHeader("vpay-name");
        if (Strings.isBlank(name)) {
            name = VpayConstant.SUPER_ACCOUNT;
        }
        Account db = accountService.findByName(name);
        account.copyFrom(db);
    }
}
