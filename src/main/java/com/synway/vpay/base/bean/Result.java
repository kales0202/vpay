package com.synway.vpay.base.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;

    private T data;

    private String msg;

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(0, data, msg);
    }

    public static <T> Result<T> success(T data) {
        return success(data, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, null, msg);
    }

    public static <T> Result<T> error(String msg) {
        return error(-1, msg);
    }

    public static <T> Result<T> error(int code) {
        return error(code, null);
    }

    public static <T> Result<T> error() {
        return error(-1, null);
    }
}
