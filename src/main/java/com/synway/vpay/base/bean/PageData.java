package com.synway.vpay.base.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询返回结果信息
 *
 * @param <K>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<K> implements BasePage {

    private long total;

    private int page;

    private int size;

    private List<K> data;

}
