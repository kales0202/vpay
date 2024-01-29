package com.synway.vpay.base.bean;

import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * 分页查询基础接口
 */
public interface BasePage {

    int getPage();

    int getSize();

    /**
     * 分页查询入参基类
     */
    @Data
    abstract class BasePageBO implements BasePage {

        /**
         * 页码
         */
        private int page = 1;

        /**
         * 每页条数
         */
        private int size = 10;

        public abstract Pageable getPageable();
    }
}
