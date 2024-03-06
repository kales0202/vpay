package com.synway.vpay.service;

import com.google.common.collect.Lists;
import com.synway.vpay.base.define.Strategy;
import com.synway.vpay.base.exception.IllegalArgumentException;
import com.synway.vpay.bean.MonitorBO;
import com.synway.vpay.bean.MonitorListBO;
import com.synway.vpay.entity.Monitor;
import com.synway.vpay.entity.PayCode;
import com.synway.vpay.enums.MonitorState;
import com.synway.vpay.enums.PayType;
import com.synway.vpay.enums.PaymentStrategy;
import com.synway.vpay.repository.MonitorRepository;
import com.synway.vpay.util.VpayConstant;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class MonitorService {
    /**
     * 监控端信息缓存
     *
     * @since 0.1
     */
    private static final Map<UUID, Map<String, Monitor>> MONITOR_STATE_MAP = new LinkedHashMap<>();

    /**
     * 账户付款码轮询策略缓存
     *
     * @since 0.1
     */
    private static final Map<UUID, Map<PayType, Strategy<PayCode>>> ACCOUNT_STRATEGY_MAP = new HashMap<>();

    @Resource
    private MonitorRepository monitorRepository;

    @Resource
    private HttpServletRequest request;

    /**
     * 根据ID查询监控端
     *
     * @param id 监控端ID
     * @return 监控端
     * @since 0.1
     */
    public Monitor findById(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("监控端ID不能为空");
        }
        return monitorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("监控端不存在"));
    }

    /**
     * 查询账户下的监控端
     *
     * @param bo 监控端查询入参
     * @return 监控端集合
     * @since 0.1
     */
    public List<Monitor> list(MonitorListBO bo) {
        Specification<Monitor> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> pl = new ArrayList<>();
            // 带上账户ID作为条件
            pl.add(criteriaBuilder.equal(root.get("accountId"), bo.getAccountId()));
            // 监控端名称
            if (Objects.nonNull(bo.getName())) {
                pl.add(criteriaBuilder.like(root.get("name"), bo.getName()));
            }
            // 按支付方式查询
            if (Objects.nonNull(bo.getType())) {
                Join<Monitor, PayCode> payCodeJoin = root.join("pay_code");
                // 查询PayCode表中的数据
                Predicate typeCondition;
                if (Objects.isNull(bo.getEnable())) {
                    typeCondition = criteriaBuilder.equal(payCodeJoin.get("type"), bo.getType());
                } else {
                    // 仅查询启用对应付款码的监控端
                    typeCondition = criteriaBuilder.and(
                            criteriaBuilder.equal(payCodeJoin.get("type"), bo.getType()),
                            criteriaBuilder.equal(payCodeJoin.get("enable"), bo.getEnable())
                    );
                }
                pl.add(criteriaBuilder.greaterThan(criteriaBuilder.count(typeCondition), 0L));
            } else if (Objects.nonNull(bo.getEnable())) {
                pl.add(criteriaBuilder.equal(root.get("enable"), bo.getEnable()));
            }

            return query.orderBy(criteriaBuilder.asc(root.get("createTime")))
                    .where(pl.toArray(new Predicate[0])).getRestriction();
        };

        List<Monitor> monitors = monitorRepository.findAll(specification);
        if (!CollectionUtils.isEmpty(monitors) && MONITOR_STATE_MAP.containsKey(bo.getAccountId())) {
            Map<String, Monitor> monitorMap = MONITOR_STATE_MAP.get(bo.getAccountId());
            for (Monitor monitor : monitors) {
                if (!monitorMap.containsKey(monitor.getName())) {
                    continue;
                }
                Monitor cache = monitorMap.get(monitor.getName());
                monitor.setState(cache.getState());
                monitor.setLastPay(cache.getLastPay());
                monitor.setLastHeart(cache.getLastHeart());
            }
        }
        return monitors;
    }

    /**
     * 查询账户下的全部监控端信息
     *
     * @return 监控端集合
     * @since 0.1
     */
    public List<Monitor> listAll(UUID accountId) {
        if (MONITOR_STATE_MAP.containsKey(accountId)) {
            return Lists.newArrayList(MONITOR_STATE_MAP.get(accountId).values());
        }
        List<Monitor> monitors = this.monitorRepository.findByAccountId(accountId);
        Map<String, Monitor> monitorMap = monitors.stream().collect(Collectors.toMap(Monitor::getName, Function.identity()));
        MONITOR_STATE_MAP.put(accountId, monitorMap);
        return monitors;
    }

    /**
     * 保存监控端
     *
     * @param monitor 监控端
     * @return 保存后的监控端
     * @since 0.1
     */
    public Monitor create(@Valid Monitor monitor) {
        monitor.setId(UUID.randomUUID());

        // 校验监控端名称是否唯一
        this.validateName(monitor.getAccountId(), monitor.getId(), monitor.getName());

        Monitor saved = monitorRepository.save(monitor);

        // 添加到缓存中
        MONITOR_STATE_MAP.get(monitor.getAccountId()).put(monitor.getName(), saved);

        return saved;
    }

    /**
     * 修改监控端
     *
     * @param bo 监控端
     * @return 修改后的监控端
     * @since 0.1
     */
    public Monitor save(MonitorBO bo) {
        Monitor monitor = this.findById(bo.getId());
        // 如果修改了名称，需要校验监控端名称是否唯一
        if (Strings.isNotBlank(bo.getName()) && !Objects.equals(bo.getName(), monitor.getName())) {
            this.validateName(monitor.getAccountId(), bo.getId(), bo.getName());
        }

        // 赋值
        bo.merge2Monitor(monitor);
        Monitor saved = monitorRepository.save(monitor);

        // 更新到缓存中
        Monitor cached = MONITOR_STATE_MAP.get(monitor.getAccountId()).get(monitor.getName());
        if (Objects.nonNull(cached)) {
            saved.setLastHeart(cached.getLastHeart());
            saved.setLastPay(cached.getLastPay());
            saved.setState(cached.getState());
        }
        MONITOR_STATE_MAP.get(monitor.getAccountId()).put(monitor.getName(), saved);

        return saved;
    }

    /**
     * 删除监控端
     *
     * @param id 监控端ID
     * @since 0.1
     */
    public void delete(UUID id, UUID accountId, PaymentStrategy strategy) {
        Monitor monitor = monitorRepository.findById(id).orElse(null);
        if (Objects.isNull(monitor)) {
            return;
        }

        // 删除监控端状态缓存
        this.removeMonitorCache(accountId, monitor.getName());

        // 如果监控端中配置了付款码，则需要刷新缓存
        monitor.getPayCodes().stream()
                .filter(PayCode::isAvailable).findFirst()
                .ifPresent(payCode -> this.resetStrategyCache(accountId, strategy));

        monitorRepository.delete(monitor);
    }

    /**
     * 校验此监控端名称是否可用，不可用时抛出IllegalArgumentException异常
     *
     * @param accountId 账户ID
     * @param id        当前监控端ID
     * @param name      监控端名称
     * @since 0.1
     */
    public void validateName(UUID accountId, UUID id, String name) {
        int count = monitorRepository.countByAccountIdAndIdNotAndName(accountId, id, name);
        if (count > 0) {
            throw new IllegalArgumentException("名称已存在，不能重复添加");
        }
    }

    /**
     * 更新监控端信息：最后支付时间
     *
     * @param accountId 账户ID
     * @param name      监控端名称
     * @param lastPay   最后支付时间
     * @since 0.1
     */
    public void updateLastPay(UUID accountId, String name, LocalDateTime lastPay) {
        if (!MONITOR_STATE_MAP.containsKey(accountId)) {
            throw new IllegalArgumentException("更新监控端心跳失败，账户不存在");
        }
        Map<String, Monitor> monitorMap = MONITOR_STATE_MAP.get(accountId);
        if (!monitorMap.containsKey(name)) {
            throw new IllegalArgumentException("更新监控端心跳失败，监控端不存在");
        }
        monitorMap.get(name).setLastPay(lastPay);
    }

    /**
     * 更新监控端信息：监控端状态
     *
     * @param accountId 账户ID
     * @param name      监控端名称
     * @param state     监控端状态
     * @param lastBeat  最后支付时间
     * @since 0.1
     */
    public Monitor updateMonitorState(UUID accountId, String name, MonitorState state, LocalDateTime lastBeat) {
        if (!MONITOR_STATE_MAP.containsKey(accountId)) {
            throw new IllegalArgumentException("更新监控端心跳失败，账户不存在");
        }
        Map<String, Monitor> monitorMap = MONITOR_STATE_MAP.get(accountId);
        if (!monitorMap.containsKey(name)) {
            throw new IllegalArgumentException("更新监控端心跳失败，监控端不存在");
        }
        monitorMap.get(name).setState(state);
        if (Objects.nonNull(lastBeat)) {
            monitorMap.get(name).setLastHeart(lastBeat);
        }
        return monitorMap.get(name);
    }

    /**
     * 获取账户下全部监控端信息
     *
     * @param accountId 账户ID
     * @since 0.1
     */
    public List<Monitor> getMonitors(UUID accountId) {
        if (!MONITOR_STATE_MAP.containsKey(accountId)) {
            throw new IllegalArgumentException("获取监控端信息失败，账户不存在");
        }
        return Lists.newArrayList(MONITOR_STATE_MAP.get(accountId).values());
    }

    /**
     * 获取监控端信息
     *
     * @param accountId 账户ID
     * @param name      监控端名称
     * @since 0.1
     */
    public Monitor getMonitor(UUID accountId, String name) {
        if (!MONITOR_STATE_MAP.containsKey(accountId)) {
            throw new IllegalArgumentException("获取监控端信息失败，账户不存在");
        }
        Map<String, Monitor> monitorMap = MONITOR_STATE_MAP.get(accountId);
        if (!monitorMap.containsKey(name)) {
            throw new IllegalArgumentException("获取监控端信息失败，监控端不存在");
        }
        return monitorMap.get(name);
    }

    public String getNameFromHeader() {
        String name = request.getHeader("Vpay-Monitor");
        if (Strings.isBlank(name)) {
            name = VpayConstant.DEFAULT_MONITOR_NAME;
        }
        return name;
    }

    public PayCode nextPayCode(UUID accountId, PayType payType, PaymentStrategy strategy) {
        if (!ACCOUNT_STRATEGY_MAP.containsKey(accountId)) {
            resetStrategyCache(accountId, strategy);
        }
        Strategy<PayCode> codeStrategy = ACCOUNT_STRATEGY_MAP.get(accountId).get(payType);
        // 判断codeStrategy是否是strategy的clazz
        Class<? extends Strategy> clazz = strategy.getClazz();
        if (!clazz.isInstance(codeStrategy)) {
            log.info("支付策略与缓存不匹配，重置支付策略");
            resetStrategyCache(accountId, strategy);
            return ACCOUNT_STRATEGY_MAP.get(accountId).get(payType).next();
        }

        return codeStrategy.next();
    }

    public synchronized void resetStrategyCache(UUID accountId, PaymentStrategy strategy) {
        // 同步执行，以accountId作为锁
        List<Monitor> monitors = this.listAll(accountId);
        if (monitors.isEmpty()) {
            throw new IllegalArgumentException("请您先配置支付地址");
        }

        List<PayCode> wechatCodes = this.getAvailablePayCodes(monitors, PayType.WECHAT);
        List<PayCode> alipayCodes = this.getAvailablePayCodes(monitors, PayType.ALIPAY);
        Map<PayType, Strategy<PayCode>> strategyMap = new HashMap<>();
        strategyMap.put(PayType.WECHAT, strategy.newInstance(wechatCodes));
        strategyMap.put(PayType.ALIPAY, strategy.newInstance(alipayCodes));

        ACCOUNT_STRATEGY_MAP.put(accountId, strategyMap);
    }

    public synchronized void removeMonitorCache(UUID accountId, String monitorName) {
        MONITOR_STATE_MAP.get(accountId).remove(monitorName);
    }

    private List<PayCode> getAvailablePayCodes(List<Monitor> monitors, PayType payType) {
        return monitors.stream()
                .filter(Monitor::isEnable)
                .flatMap(monitor -> monitor.getPayCodes().stream())
                .filter(payCode -> Objects.equals(payCode.getType(), payType))
                .filter(PayCode::isAvailable)
                .sorted(Comparator.comparing(PayCode::getWeight))
                .toList();
    }
}
