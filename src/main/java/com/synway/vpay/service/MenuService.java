package com.synway.vpay.service;

import com.synway.vpay.entity.Menu;
import com.synway.vpay.repository.MenuRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@Validated
public class MenuService {

    @Resource
    private MenuRepository menuRepository;

    @Cacheable(value = "menus", key = "#root.methodName")
    public List<Menu> tree() {
        return menuRepository.findRoots();
    }
}
