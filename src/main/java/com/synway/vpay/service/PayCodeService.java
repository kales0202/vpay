package com.synway.vpay.service;

import com.synway.vpay.base.exception.IllegalArgumentException;
import com.synway.vpay.bean.PayCodeBO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.PayCode;
import com.synway.vpay.repository.PayCodeRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
public class PayCodeService {

    @Resource
    private PayCodeRepository payCodeRepository;

    @Resource
    private Account account;

    /**
     * 根据ID查询付款码
     *
     * @param id 付款码ID
     * @return 付款码
     * @since 0.1
     */
    public PayCode findById(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("付款码ID不能为空");
        }
        return payCodeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("付款码不存在"));
    }

    /**
     * 查询账户下的付款码
     *
     * @param bo 付款码查询入参
     * @return 付款码集合
     * @since 0.1
     */
    public List<PayCode> list(PayCodeBO bo) {
        Specification<PayCode> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> pl = new ArrayList<>();
            // 带上账户ID作为条件
            pl.add(criteriaBuilder.equal(root.get("accountId"), account.getId()));
            // 付款码名称
            if (Objects.nonNull(bo.getName())) {
                pl.add(criteriaBuilder.like(root.get("name"), bo.getName()));
            }
            // 付款码类型
            if (Objects.nonNull(bo.getPayType())) {
                pl.add(criteriaBuilder.equal(root.get("payType"), bo.getPayType()));
            }
            // 付款码类别
            if (Objects.nonNull(bo.getCodeType())) {
                pl.add(criteriaBuilder.equal(root.get("codeType"), bo.getCodeType()));
            }
            // 是否启用
            if (Objects.nonNull(bo.getEnabled())) {
                pl.add(criteriaBuilder.equal(root.get("enabled"), bo.getEnabled()));
            }

            return query.orderBy(
                            criteriaBuilder.asc(root.get("payType")),
                            criteriaBuilder.desc(root.get("enabled")),
                            criteriaBuilder.desc(root.get("weight"))
                    )
                    .where(pl.toArray(new Predicate[0])).getRestriction();
        };

        return payCodeRepository.findAll(specification);
    }

    /**
     * 保存付款码
     *
     * @param payCode 付款码
     * @return 保存后的付款码
     * @since 0.1
     */
    public PayCode create(@Valid PayCode payCode) {
        payCode.setId(UUID.randomUUID());

        // 校验付款码名称是否唯一
        this.validateName(payCode);

        // 校验付款码是否唯一
        this.validateContent(payCode);

        return payCodeRepository.save(payCode);
    }


    /**
     * 修改付款码
     *
     * @param bo 付款码
     * @return 修改后的付款码
     * @since 0.1
     */
    public PayCode save(@Valid PayCodeBO bo) {
        PayCode payCode = this.findById(bo.getId());
        // 如果修改了名称，需要校验付款码名称是否唯一
        if (Strings.isNotBlank(bo.getName()) && !Objects.equals(payCode.getName(), bo.getName())) {
            payCode.setName(bo.getName());
            this.validateName(payCode);
        }

        // 赋值
        bo.toPayCode(payCode);
        return payCodeRepository.save(payCode);
    }

    /**
     * 删除付款码
     *
     * @param id 付款码ID
     * @since 0.1
     */
    public void deleteById(UUID id) {
        payCodeRepository.deleteById(id);
    }

    public void validateName(PayCode payCode) {
        int count = payCodeRepository.countByIdNotAndAccountIdAndName(payCode.getId(), account.getId(), payCode.getName());
        if (count > 0) {
            throw new IllegalArgumentException("名称已存在，不能重复添加");
        }
    }

    public void validateContent(PayCode payCode) {
        PayCode existed = payCodeRepository.findByIdNotAndAccountIdAndContent(payCode.getId(), account.getId(), payCode.getContent());
        if (Objects.nonNull(existed)) {
            throw new IllegalArgumentException("付款码已存在请勿重复添加，名称为：" + existed.getName());
        }
    }
}
