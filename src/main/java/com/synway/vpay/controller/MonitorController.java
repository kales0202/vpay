package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.MonitorBO;
import com.synway.vpay.bean.MonitorListBO;
import com.synway.vpay.bean.MonitorVO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Monitor;
import com.synway.vpay.service.MonitorService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 监控端接口
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Resource
    private MonitorService monitorService;

    @Resource
    private Account account;

    /**
     * 新增监控端
     *
     * @param bo 监控端信息
     * @return 新增后的监控端
     * @since 0.1
     */
    @PostMapping
    public Result<Monitor> create(@RequestBody MonitorBO bo) {
        Monitor monitor = bo.merge2Monitor(new Monitor());
        monitor.setAccountId(account.getId());
        return Result.success(monitorService.create(monitor));
    }

    /**
     * 修改监控端信息
     *
     * @param bo 监控端修改入参
     * @return 修改后的监控端
     * @since 0.1
     */
    @PutMapping
    public Result<Monitor> save(@RequestBody MonitorBO bo) {
        return Result.success(monitorService.save(bo));
    }

    /**
     * 根据ID删除监控端
     *
     * @param id 监控端ID
     * @since 0.1
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable UUID id) {
        monitorService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询监控端集合
     *
     * @param bo 监控端查询入参
     * @return 监控端集合
     * @since 0.1
     */
    @PostMapping("/list")
    public Result<List<MonitorVO>> list(MonitorListBO bo) {
        List<Monitor> monitors = monitorService.list(bo);
        return Result.success(monitors.stream().map(MonitorVO::from).toList());
    }
}
