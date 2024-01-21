package com.synway.vpay.base.exception;

public class BusinessException extends RuntimeException {
    public static final int CODE = -1;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return -1;
    }
}
