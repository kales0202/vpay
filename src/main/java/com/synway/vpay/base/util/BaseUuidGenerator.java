package com.synway.vpay.base.util;

import com.synway.vpay.base.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;
import org.hibernate.type.descriptor.java.UUIDJavaType;
import org.hibernate.type.descriptor.java.UUIDJavaType.ValueTransformer;

import java.lang.reflect.Member;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

import static org.hibernate.generator.EventTypeSets.INSERT_ONLY;
import static org.hibernate.internal.util.ReflectHelper.getPropertyType;

@Slf4j
public class BaseUuidGenerator implements BeforeExecutionGenerator {
    private final ValueTransformer valueTransformer;

    private BaseUuidGenerator(Member idMember) {
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

    public BaseUuidGenerator(
            com.synway.vpay.base.annotation.BaseUuidGenerator config,
            Member idMember,
            CustomIdGeneratorCreationContext creationContext) {
        this(idMember);
    }

    public BaseUuidGenerator(
            com.synway.vpay.base.annotation.BaseUuidGenerator config,
            Member member,
            GeneratorCreationContext creationContext) {
        this(member);
    }

    /**
     * @return {@link EventTypeSets#INSERT_ONLY}
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