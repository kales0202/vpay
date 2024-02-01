package com.synway.vpay.controller;


import com.synway.vpay.util.VpayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 静态页面
 *
 * @since 0.1
 */
@Controller
@RequestMapping("/")
public class ResourceController {

    /**
     * 首页
     *
     * @return 首页
     * @since 0.1
     */
    @GetMapping()
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
     * 登录页
     *
     * @return 登录页
     * @since 0.1
     */
    @GetMapping({"404"})
    public String notFound() {
        return "redirect:/" + VpayUtil.getTemplateConfig().getNotFound();
    }

    @GetMapping("/pay")
    public String pay(String order) {
        return "redirect:/" + VpayUtil.getTemplateConfig().getPay() + "?order=" + order;
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
