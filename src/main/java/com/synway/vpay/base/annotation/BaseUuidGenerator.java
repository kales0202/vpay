package com.synway.vpay.base.annotation;


import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@IdGeneratorType(com.synway.vpay.base.util.BaseUuidGenerator.class)
@ValueGenerationType(generatedBy = com.synway.vpay.base.util.BaseUuidGenerator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface BaseUuidGenerator {
}
