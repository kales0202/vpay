package com.synway.vpay.bean;

import com.synway.vpay.entity.Monitor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

/**
 * 监控端保存/修改入参
 *
 * @since 0.1
 */
@Data
@NoArgsConstructor
public class MonitorBO {

    /**
     * 监控端ID
     *
     * @since 0.1
     */
    private UUID id;

    /**
     * 唯一标识名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 是否启用
     *
     * @since 0.1
     */
    private Boolean enable;

    public Monitor merge2Monitor(Monitor monitor) {
        if (Objects.nonNull(name)) {
            monitor.setName(name);
        }
        if (Objects.nonNull(enable)) {
            monitor.setEnable(enable);
        }
        return monitor;
    }
}
