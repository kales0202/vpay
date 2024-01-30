package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.bean.OrderQueryBO;
import com.synway.vpay.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class OrderRepository extends BaseRepository<Order, UUID> {

    public OrderRepository(EntityManager entityManager) {
        super(Order.class, entityManager);
    }

    /**
     * 分页查询
     *
     * @param bo orderBO
     * @return 订单分页数据集合
     * @since 0.1
     */
    public Page<Order> findAll(OrderQueryBO bo) {
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
        return this.findAll(specification, bo.getPageable());
    }
}
