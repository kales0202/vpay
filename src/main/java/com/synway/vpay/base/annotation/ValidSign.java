package com.synway.vpay.base.annotation;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.synway.vpay.base.bean.SignBo;
import com.synway.vpay.base.exception.SignatureException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSign {

    Class<? extends Executor> value();

    interface Executor {

        /**
         * 验签失败时，请抛出SignatureException异常
         *
         * @param bo SignBo
         * @throws SignatureException 验签失败抛出异常
         * @since 0.1
         */
        void execute(SignBo bo) throws SignatureException;
    }

    @Slf4j
    @Aspect
    @Component
    class ValidSignAspect {
        private static final Cache<Class<? extends Executor>, Executor> EXECUTOR_CACHE = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500)
                .build();

        @Resource
        private ApplicationContext applicationContext;

        @Before("@within(ValidSign) || @annotation(ValidSign)")
        public void validate(JoinPoint joinPoint) throws ExecutionException {
            Object[] args = joinPoint.getArgs();

            ValidSign validSign = this.getValidSign(joinPoint);
            if (Objects.isNull(validSign)) {
                return;
            }

            Executor executorBean = EXECUTOR_CACHE.get(validSign.value(), () -> applicationContext.getBean(validSign.value()));

            boolean valid = false;
            for (Object arg : args) {
                if (Objects.nonNull(arg) && arg instanceof SignBo bo) {
                    valid = true;
                    executorBean.execute(bo);
                }
            }
            if (!valid) {
                executorBean.execute(null);
            }
        }

        private ValidSign getValidSign(JoinPoint joinPoint) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            ValidSign validSign = signature.getMethod().getAnnotation(ValidSign.class);
            if (Objects.isNull(validSign)) {
                Class<?> target = joinPoint.getTarget().getClass();
                validSign = target.getAnnotation(ValidSign.class);
            }
            return validSign;
        }
    }
}
