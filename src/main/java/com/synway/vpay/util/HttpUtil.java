package com.synway.vpay.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class HttpUtil {

    /**
     * 发送get请求
     *
     * @param url 请求地址
     * @return 返回响应体
     * @since 0.1
     */
    public static String get(String url) {
        return get(url, String.class);
    }

    /**
     * 发送get请求
     *
     * @param url   请求地址
     * @param clazz 响应类
     * @param <T>   响应类型
     * @return 返回响应体
     * @since 0.1
     */
    public static <T> T get(String url, Class<T> clazz) {
        T result = null;
        try {
            result = WebClient.create(url).get().retrieve().bodyToMono(clazz).block();
        } catch (Exception e) {
            log.error("get error: " + url, e);
        }
        return result;
    }

    /**
     * 发送post请求
     *
     * @param url   请求地址
     * @param param 请求体参数
     * @param clazz 响应类
     * @param <T>   响应类型
     * @param <K>   请求体类型
     * @return 返回响应体
     * @since 0.1
     */
    public static <T, K> T post(String url, K param, Class<T> clazz) {
        T result = null;
        try {
            result = WebClient.create(url).post().bodyValue(param).retrieve().bodyToMono(clazz).block();
        } catch (Exception e) {
            log.error("get error: " + url, e);
        }
        return result;
    }

    /**
     * 将map转换为get请求参数
     *
     * @param map map
     * @return string
     * @since 0.1
     */
    public static String map2GetParam(Map<String, String> map) {
        return map2GetParam(null, map);
    }

    /**
     * 将map转换为get请求参数，拼接在url后
     *
     * @param url 请求地址
     * @param map map
     * @return string
     * @since 0.1
     */
    public static String map2GetParam(String url, Map<String, String> map) {
        url = Strings.isBlank(url) ? "" : url;
        String params = map.entrySet().stream()
                .filter(e -> Strings.isNotBlank(e.getKey()) && Strings.isNotBlank(e.getValue()))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        if (Strings.isBlank(params)) {
            return url;
        }
        if (url.contains("?")) {
            return url + "&" + params;
        } else {
            return url + "?" + params;
        }
    }
}
