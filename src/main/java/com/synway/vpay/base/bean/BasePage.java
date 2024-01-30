package com.synway.vpay.base.bean;

import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * 分页查询基础接口
 *
 * @since 0.1
 */
public interface BasePage {

    /**
     * @return 当前页码
     * @since 0.1
     */
    int getPage();

    /**
     * @return 每页条数
     * @since 0.1
     */
    int getSize();

    /**
     * 分页查询入参基类
     *
     * @since 0.1
     */
    @Data
    abstract class BasePageBO implements BasePage {

        /**
         * 页码
         *
         * @since 0.1
         */
        private int page = 1;

        /**
         * 每页条数
         *
         * @since 0.1
         */
        private int size = 10;

        /**
         * @return Pageable
         */
        public abstract Pageable getPageable();
    }
}
