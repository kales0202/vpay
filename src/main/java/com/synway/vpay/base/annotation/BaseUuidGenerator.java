package com.synway.vpay.base.annotation;


import com.synway.vpay.base.annotation.BaseUuidGenerator.UuidGenerator;
import com.synway.vpay.base.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.ValueGenerationType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;
import org.hibernate.type.descriptor.java.UUIDJavaType;
import org.hibernate.type.descriptor.java.UUIDJavaType.ValueTransformer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.generator.EventTypeSets.INSERT_ONLY;
import static org.hibernate.internal.util.ReflectHelper.getPropertyType;

/**
 * 自定义UUID生成器
 * 参考了org.hibernate.annotations.UuidGenerator的实现
 * 与org.hibernate.annotations.UuidGenerator的区别只有一个
 * 当实体类型是com.synway.vpay.base.entity.BaseEntity时，如果实体已经有id了，不会覆盖
 *
 * @see org.hibernate.annotations.UuidGenerator
 * @since 0.1
 */
@IdGeneratorType(UuidGenerator.class)
@ValueGenerationType(generatedBy = UuidGenerator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface BaseUuidGenerator {

    /**
     * 自定义UUID生成器实现
     *
     * @see org.hibernate.annotations.UuidGenerator
     * @since 0.1
     */
    @Slf4j
    class UuidGenerator implements BeforeExecutionGenerator {
        private final ValueTransformer valueTransformer;

        private UuidGenerator(Member idMember) {
            final Class<?> propertyType = getPropertyType(idMember);
            if (UUID.class.isAssignableFrom(propertyType)) {
                valueTransformer = UUIDJavaType.PassThroughTransformer.INSTANCE;
            } else if (String.class.isAssignableFrom(propertyType)) {
                valueTransformer = UUIDJavaType.ToStringTransformer.INSTANCE;
            } else if (byte[].class.isAssignableFrom(propertyType)) {
                valueTransformer = UUIDJavaType.ToBytesTransformer.INSTANCE;
            } else {
                throw new HibernateException("Unanticipated return type [" + propertyType.getName() + "] for UUID conversion");
            }
        }

        public UuidGenerator(
                com.synway.vpay.base.annotation.BaseUuidGenerator config,
                Member idMember,
                CustomIdGeneratorCreationContext creationContext) {
            this(idMember);
        }

        public UuidGenerator(
                com.synway.vpay.base.annotation.BaseUuidGenerator config,
                Member member,
                GeneratorCreationContext creationContext) {
            this(member);
        }

        /**
         * @return {@link EventTypeSets#INSERT_ONLY}
         * @since 0.1
         */
        @Override
        public EnumSet<EventType> getEventTypes() {
            return INSERT_ONLY;
        }

        @Override
        public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
            if (owner instanceof BaseEntity) {
                currentValue = ((BaseEntity) owner).getId();
            }
            if (Objects.nonNull(currentValue)) {
                return currentValue;
            }
            return valueTransformer.transform(UUID.randomUUID());
        }
    }
}