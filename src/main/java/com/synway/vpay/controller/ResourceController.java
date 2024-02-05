package com.synway.vpay.controller;


import com.synway.vpay.util.VpayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 静态页面、无需登录的接口
 *
 * @since 0.1
 */
@Controller
public class ResourceController {

    /**
     * 首页
     *
     * @return 首页
     * @since 0.1
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/" + VpayUtil.getTemplateConfig().getIndex();
    }

    /**
     * 登录页
     *
     * @return 登录页
     * @since 0.1
     */
    @GetMapping({"login"})
    public String login() {
        return "redirect:/" + VpayUtil.getTemplateConfig().getLogin();
    }

    /**
     * 支付页地址
     *
     * @param order 订单ID
     * @return 支付页地址
     * @since 0.1
     */
    @GetMapping("/pay")
    public String pay(String order) {
        return "redirect:/" + VpayUtil.getTemplateConfig().getPay() + "?order=" + order;
    }

    /**
     * 404
     *
     * @return 登录页
     * @since 0.1
     */
    @GetMapping({"404"})
    public String notFound() {
        return "redirect:/" + VpayUtil.getTemplateConfig().getNotFound();
    }

    /**
     * favicon.ico
     *
     * @since 0.1
     */
    @GetMapping({"favicon.ico", "favicon"})
    @ResponseBody
    public void returnNoFavicon() {
    }
}
