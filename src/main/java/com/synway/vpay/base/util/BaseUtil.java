package com.synway.vpay.base.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class BaseUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String object2Json(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static <T> T json2Object(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> object2Map(Object obj) {
        if (Objects.isNull(obj)) {
            return Collections.emptyMap();
        }
        try {
            return OBJECT_MAPPER.convertValue(obj, Map.class);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static <T> T map2Object(Map<String, Object> map, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.convertValue(map, clazz);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
