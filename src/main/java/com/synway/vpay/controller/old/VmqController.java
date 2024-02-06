package com.synway.vpay.controller.old;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.base.define.IBaseEnum;
import com.synway.vpay.base.exception.IllegalArgumentException;
import com.synway.vpay.base.exception.SignatureException;
import com.synway.vpay.base.util.BaseUtil;
import com.synway.vpay.bean.AccountState;
import com.synway.vpay.bean.OrderCreateBO;
import com.synway.vpay.controller.PublicController;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.service.AccountService;
import com.synway.vpay.service.OrderService;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 旧的VMQ接口
 *
 * @since 0.1
 */
@RestController
@Deprecated
public class VmqController {

    @Resource
    private AccountService accountService;

    @Resource
    private OrderService orderService;

    @Resource
    private Account account;

    /**
     * 创建订单（仅超级管理员）
     *
     * @param payId     商户订单号
     * @param param     订单保存的信息
     * @param type      支付方式 1|微信 2|支付宝
     * @param price     订单价格
     * @param notifyUrl 异步通知地址，如果为空则使用系统后台设置的地址
     * @param returnUrl 支付完成后同步跳转地址，将会携带参数跳转
     * @param sign      签名认证 签名方式为 md5(payId + param + type + price + 通讯密钥)
     * @param isHtml    0返回json数据 1跳转到支付页面
     * @return 订单信息
     * @see PublicController#createOrder
     * @since 0.1
     */
    @RequestMapping("/createOrder")
    public String createOrder(String payId, String param, Integer type, String price,
                              String notifyUrl, String returnUrl, String sign, int isHtml) {

        PayType payType = IBaseEnum.fromValue(PayType.class, type);
        if (Objects.isNull(payType)) {
            throw new IllegalArgumentException("支付方式不正确: " + type);
        }

        OrderCreateBO bo = new OrderCreateBO();
        bo.setPayId(payId);
        bo.setParam(param);
        bo.setPayType(payType);
        bo.setPrice(new BigDecimal(price));
        bo.setNotifyUrl(notifyUrl);
        bo.setReturnUrl(returnUrl);
        bo.setSign(sign);
        bo.setIsHtml(isHtml);
        bo.setAccountName(VpayConstant.SUPER_ACCOUNT);

        // 校验签名
        this.simulatedLogin((ac) -> {
            String cSign = VpayUtil.md5(bo.getPayId() + bo.getParam() + bo.getPayType().getValue()
                    + bo.getPrice() + ac.getKeyword());
            if (!Objects.equals(bo.getSign(), cSign)) {
                throw new SignatureException();
            }
        });

        Order order = orderService.create(bo);
        if (isHtml == 0) {
            return BaseUtil.object2Json(this.success(order));
        }
        return "<script>window.location.href = '/pay?orderId=" + order.getOrderId() + "'</script>";
    }

    /**
     * 查询服务端状态（仅超级管理员）
     *
     * @param t    现行时间戳
     * @param sign md5(现行时间戳+通讯密钥)
     * @return 服务端状态
     * @see PublicController#getAccountState
     * @since 0.1
     */
    @RequestMapping("/getState")
    public Result<Map<String, Object>> getState(String t, String sign) {
        if (t == null) {
            return Result.error("请传入t");
        }
        if (sign == null) {
            return Result.error("请传入sign");
        }

        // 校验签名
        this.simulatedLogin((ac) -> {
            if (!Objects.equals(VpayUtil.md5(t + ac.getKeyword()), sign)) {
                throw new SignatureException();
            }
        });

        // 获取账户状态
        AccountState state = VpayUtil.getAccountState(VpayConstant.SUPER_ID);

        Map<String, Object> data = new HashMap<>();
        data.put("lastpay", VpayUtil.toTimestamp(state.getLastPay()));
        data.put("lastheart", VpayUtil.toTimestamp(state.getLastHeart()));
        data.put("state", state.getMonitorState().getValue());
        return this.success(data);
    }

    /**
     * 获取订单信息（仅超级管理员）
     *
     * @param orderId 订单ID
     * @return 订单信息
     * @see PublicController#getOrder
     * @since 0.1
     */
    @RequestMapping("/getOrder")
    public Result<Order> getOrder(String orderId) {
        if (orderId == null) {
            return Result.error("请传入订单编号");
        }
        this.simulatedLogin((ac) -> {
        });
        return this.success(orderService.findByOrderId(orderId));
    }

    /**
     * 查询订单状态（仅超级管理员）
     *
     * @param orderId 订单ID
     * @return 订单支付完成后的跳转地址（带回调参数）
     * @see PublicController#checkOrder
     * @since 0.1
     */
    @RequestMapping("/checkOrder")
    public Result<String> checkOrder(String orderId) {
        if (orderId == null) {
            return Result.error("请传入订单编号");
        }
        this.simulatedLogin((ac) -> {
        });
        return this.success(orderService.checkOrder(orderId));
    }

    /**
     * 关闭订单（仅超级管理员）
     *
     * @param orderId 订单ID
     * @see PublicController#closeOrder
     * @since 0.1
     */
    @RequestMapping("/closeOrder")
    public Result<Void> closeOrder(String orderId, String sign) {
        if (orderId == null) {
            return Result.error("请传入订单编号");
        }
        if (sign == null) {
            return Result.error("请传入sign");
        }

        // 校验签名
        this.simulatedLogin((ac) -> {
            if (!Objects.equals(VpayUtil.md5(orderId + ac.getKeyword()), sign)) {
                throw new SignatureException();
            }
        });
        orderService.closeOrder(orderId);
        return this.success();
    }

    public void simulatedLogin(Consumer<Account> consumer) {
        Account ac = accountService.findById(VpayConstant.SUPER_ID);
        consumer.accept(ac);
        account.copyFrom(ac);
    }

    private <T> Result<T> success() {
        return this.success(null);
    }

    private <T> Result<T> success(T data) {
        Result<T> success = Result.success(data);
        success.setCode(1); // vmq接口返回1表示成功
        return success;
    }
}
