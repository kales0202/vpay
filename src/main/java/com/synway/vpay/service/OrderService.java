package com.synway.vpay.service;

import com.synway.vpay.base.bean.PageData;
import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.base.exception.IllegalArgumentException;
import com.synway.vpay.bean.OrderCreateBO;
import com.synway.vpay.bean.OrderDeleteBO;
import com.synway.vpay.bean.OrderQueryBO;
import com.synway.vpay.bean.OrderStatisticsVO;
import com.synway.vpay.entity.Account;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.enums.PayQRCodeType;
import com.synway.vpay.exception.FulfillException;
import com.synway.vpay.repository.OrderRepository;
import com.synway.vpay.util.HttpUtil;
import com.synway.vpay.util.VpayUtil;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {
    @Resource
    private OrderRepository orderRepository;

    @Resource
    private TempPriceService tempPriceService;

    @Resource
    private EntityManager entityManager;

    @Resource
    private Account account;

    /**
     * 保存订单
     *
     * @param bo 订单信息
     * @return 订单数据
     * @since 0.1
     */
    public Order save(OrderCreateBO bo) {
        if (this.countByPayId(bo.getPayId()) > 0) {
            throw new BusinessException("商户订单号已存在");
        }

        String payUrl = account.getPayUrl(bo.getPayType());
        if (Strings.isBlank(payUrl)) {
            throw new BusinessException("请您先配置支付地址");
        }

        Order order = bo.toOrder();
        order.setAccountId(account.getId());
        order.setRealPrice(tempPriceService.saveRealPrice(order.getAccountId(), order.getPayType(), order.getPrice()));
        order.setPayUrl(payUrl);
        order.setIsAuto(PayQRCodeType.GENERIC);
        order.setState(OrderState.WAIT);

        return orderRepository.save(order);
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public int deleteByIds(List<UUID> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return 0;
        }
        return this.delete(new OrderDeleteBO(ids));
    }

    @Transactional
    public int delete(OrderDeleteBO bo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Order> criteriaDelete = criteriaBuilder.createCriteriaDelete(Order.class);
        Root<Order> root = criteriaDelete.from(Order.class);
        // 如果传入了订单ID，则直接根据ID删除数据，忽略其它条件
        if (!ObjectUtils.isEmpty(bo.getIds())) {
            criteriaDelete.where(criteriaBuilder.in(root.get("id")).value(bo.getIds()));
            return entityManager.createQuery(criteriaDelete).executeUpdate();
        }
        // 根据时间范围删除数据
        if (Objects.nonNull(bo.getStartTime()) && Objects.nonNull(bo.getEndTime())) {
            criteriaDelete.where(criteriaBuilder.between(root.get("createTime"), bo.getStartTime(), bo.getEndTime()));
        }
        // 根据订单状态删除数据
        if (Objects.nonNull(bo.getState())) {
            criteriaDelete.where(criteriaBuilder.equal(root.get("state"), bo.getState()));
        }
        // 根据支付类型删除数据
        if (Objects.nonNull(bo.getPayType())) {
            criteriaDelete.where(criteriaBuilder.equal(root.get("payType"), bo.getPayType()));
        }
        // 无删除条件时，不执行删除操作（不允许全量删除订单）
        if (Objects.isNull(criteriaDelete.getRestriction())) {
            return 0;
        }
        return entityManager.createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * 分页查询
     *
     * @param bo orderBO
     * @return 订单分页数据集合
     * @since 0.1
     */
    public PageData<Order> findAll(OrderQueryBO bo) {
        Specification<Order> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> pl = new ArrayList<>();
            if (Objects.nonNull(bo.getPayType())) {
                pl.add(criteriaBuilder.equal(root.get("payType"), bo.getPayType()));
            }
            if (Objects.nonNull(bo.getState())) {
                pl.add(criteriaBuilder.equal(root.get("state"), bo.getState()));
            }
            return query.where(pl.toArray(new Predicate[0])).getRestriction();
        };

        Page<Order> orders = orderRepository.findAll(specification, bo.getPageable());
        return new PageData<>(orders.getTotalElements(), bo.getPage(), bo.getSize(), orders.getContent());
    }

    public OrderStatisticsVO statistics() {
        LocalDate DateNow = LocalDate.now();
        LocalDateTime startTime = LocalDateTime.of(DateNow, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(DateNow, LocalTime.MAX);

        OrderStatisticsVO vo = new OrderStatisticsVO();
        // 今日总订单数量(=成功订单+失败订单+进行中订单)
        vo.setTodayTotal(this.countTotal(startTime, endTime));
        // 今日成功订单数量
        vo.setTodaySuccess(this.countSuccess(startTime, endTime));
        // 今日失败订单数量
        vo.setTodayFailure(this.countFailure(startTime, endTime));
        // 总成功订单数量
        vo.setTotalSuccess(this.countByState(OrderState.SUCCESS));
        // 总失败订单数量
        vo.setTotalFailure(this.countByState(OrderState.EXPIRED));
        // 今日收入
        vo.setTodayIncome(this.sumPrice(startTime, endTime));
        // 总收入
        vo.setTotalIncome(this.sumPrice());
        return vo;
    }

    /**
     * 统计时间范围内的订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计时间范围内的订单数量
     * @since 0.1
     */
    public long countTotal(LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.countByCreateTimeBetween(startTime, endTime);
    }

    /**
     * 统计时间范围内的成功订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计的订单数量
     * @since 0.1
     */
    public long countSuccess(LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.countByStateAndCreateTimeBetween(OrderState.SUCCESS, startTime, endTime);
    }

    /**
     * 统计时间范围内的失败订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计的订单数量
     * @since 0.1
     */
    public long countFailure(LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.countByStateAndCreateTimeBetween(OrderState.EXPIRED, startTime, endTime);
    }

    /**
     * 根据订单状态统计订单数量
     *
     * @param state 订单状态
     * @return 统计的订单数量
     * @since 0.1
     */
    public long countByState(OrderState state) {
        return orderRepository.countByState(state);
    }

    /**
     * 根据时间范围统计成功的订单总额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计的成功订单总额
     * @since 0.1
     */
    private BigDecimal sumPrice(LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.sumPrice(startTime, endTime);
    }

    /**
     * 统计成功的订单总额
     *
     * @return 统计的成功订单总额
     * @since 0.1
     */
    private BigDecimal sumPrice() {
        return orderRepository.sumPrice();
    }

    /**
     * 补单
     *
     * @return 补单失败时的异常信息
     * @since 0.1
     */
    public String fulfill(UUID id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException("订单不存在");
        }

        String url = order.getNotifyUrl();
        if (url == null || url.equals("")) {
            url = account.getNotifyUrl();
            if (url == null || url.equals("")) {
                throw new BusinessException("您还未配置异步通知地址，请现在系统配置中配置");
            }
        }

        Map<String, String> params = new HashMap<>();
        String sign = order.getPayId() + order.getParam() + order.getPayType()
                + order.getPrice() + order.getRealPrice() + account.getKeyword();
        params.put("payId", order.getPayId());
        params.put("param", order.getParam());
        params.put("type", String.valueOf(order.getPayType().getValue()));
        params.put("price", order.getPrice().toString());
        params.put("reallyPrice", order.getRealPrice().toString());
        params.put("sign", VpayUtil.md5(sign));

        url = HttpUtil.map2GetParam(url, params);

        String res = HttpUtil.get(url);
        if (res != null && res.equals("success")) {
            if (order.getState() == OrderState.WAIT) {
                tempPriceService.deleteByPayTypeAndPrice(order.getPayType(), order.getRealPrice());
            }
            order.setState(OrderState.SUCCESS);
            orderRepository.save(order);
        } else {
            throw new FulfillException(res);
        }
        return res;
    }

    /**
     * 根据支付商户订单号统计订单数量
     *
     * @param payId 支付商户订单号
     * @return 统计的订单数量
     * @since 0.1
     */
    public long countByPayId(String payId) {
        return orderRepository.countByPayId(payId);
    }
}
