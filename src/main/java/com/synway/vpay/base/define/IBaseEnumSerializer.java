package com.synway.vpay.base.define;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * jackson序列化IBaseEnum，额外添加翻译字段
 *
 * @since 0.1
 */
public class IBaseEnumSerializer extends JsonSerializer<IBaseEnum> {

    private static final String TRANSLATE_LAST = "Trans";

    @Override
    public void serialize(IBaseEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            String fieldName = gen.getOutputContext().getCurrentName();
            gen.writeNumber(value.getValue());
            gen.writeStringField(this.getTransName(fieldName), value.getName());
        }
    }

    private String getTransName(String fieldName) {
        return fieldName + TRANSLATE_LAST;
    }
}
