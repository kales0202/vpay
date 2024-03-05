package com.synway.vpay.base.configuration;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.synway.vpay.base.define.IBaseEnum;
import com.synway.vpay.base.define.IBaseEnumSerializer;
import com.synway.vpay.base.define.LocalDateTimestampDeserializer;
import com.synway.vpay.base.define.LocalDateTimestampSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * Jackson配置
 *
 * @since 0.1
 */
@Configuration
public class JacksonMapperConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // 将Long类型转换成string类型返回，避免大整数导致前端精度丢失的问题
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.serializerByType(Long.class, ToStringSerializer.instance);

            // IBaseEnum 枚举字段添加翻译字段
            builder.serializerByType(IBaseEnum.class, new IBaseEnumSerializer());
            // LocalDateTime 序列化配置
            builder.serializerByType(LocalDateTime.class, new LocalDateTimestampSerializer());
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimestampDeserializer());
        };
    }
}
