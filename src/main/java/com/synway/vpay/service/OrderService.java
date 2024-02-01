package com.synway.vpay.service;

import com.synway.vpay.base.bean.PageData;
import com.synway.vpay.bean.OrderCreateBO;
import com.synway.vpay.bean.OrderQueryBO;
import com.synway.vpay.bean.OrderStatisticsVO;
import com.synway.vpay.entity.Order;
import com.synway.vpay.enums.OrderState;
import com.synway.vpay.repository.OrderRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {
    @Resource
    private OrderRepository orderRepository;

    @Resource
    private TempPriceService tempPriceService;

    public Order findById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }

    public Order save(OrderCreateBO createBO) {
        Order order = createBO.toOrder();
        order.setRealPrice(tempPriceService.getRealPrice(order.getPrice()));
        return orderRepository.save(order);
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
            Predicate predicate = criteriaBuilder.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            if (Objects.nonNull(bo.getType())) {
                expressions.add(criteriaBuilder.equal(root.get("type"), bo.getType()));
            }
            if (Objects.nonNull(bo.getState())) {
                expressions.add(criteriaBuilder.equal(root.get("state"), bo.getState()));
            }
            query.where(predicate);
            return predicate;
        };

        Example<Order> of = Example.of(new Order());

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

}
