package com.synway.vpay.controller;

import com.synway.vpay.entity.Menu;
import com.synway.vpay.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理员相关接口
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 获取树形菜单
     *
     * @return 树形菜单集合
     * @since 0.1
     */
    @GetMapping("/tree")
    public List<Menu> tree() {
        return menuService.tree();
    }
}
