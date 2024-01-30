package com.synway.vpay.base.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询返回结果信息
 *
 * @param <K>
 * @since 0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<K> implements BasePage {

    /**
     * 总数据量
     *
     * @since 0.1
     */
    private long total;

    /**
     * 当前页码
     *
     * @since 0.1
     */
    private int page;

    /**
     * 每页条数
     *
     * @since 0.1
     */
    private int size;

    /**
     * 数据集
     *
     * @since 0.1
     */
    private List<K> data;

}
