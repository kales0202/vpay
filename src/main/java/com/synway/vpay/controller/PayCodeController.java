package com.synway.vpay.controller;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.bean.PayCodeBO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.PayCode;
import com.synway.vpay.service.PayCodeService;
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
 * 付款码接口
 *
 * @since 0.1
 */
@Validated
@RestController
@RequestMapping("/pay-code")
public class PayCodeController {

    @Resource
    private PayCodeService payCodeService;

    @Resource
    private Account account;

    /**
     * 新增付款码
     *
     * @param payCode 付款码信息
     * @return 新增后的付款码
     * @since 0.1
     */
    @PostMapping
    public Result<PayCode> post(@RequestBody PayCode payCode) {
        payCode.setAccountId(account.getId());
        return Result.success(payCodeService.create(payCode));
    }

    /**
     * 修改付款码信息
     *
     * @param bo 付款码修改入参
     * @return 修改后的付款码
     * @since 0.1
     */
    @PutMapping
    public Result<PayCode> put(@RequestBody PayCodeBO bo) {
        return Result.success(payCodeService.save(bo));
    }

    /**
     * 根据ID删除付款码
     *
     * @param id 付款码ID
     * @since 0.1
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable UUID id) {
        payCodeService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/list")
    public Result<List<PayCode>> list(PayCodeBO bo) {
        return Result.success(payCodeService.list(bo));
    }
}
