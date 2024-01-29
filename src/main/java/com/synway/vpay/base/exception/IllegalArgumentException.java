package com.synway.vpay.base.exception;

public class IllegalArgumentException extends BusinessException {

    public static final int CODE = -4;
    public static final String MESSAGE = "非法参数...";

    public IllegalArgumentException() {
        super(MESSAGE);
    }

    public IllegalArgumentException(String message) {
        super(message);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public int getCode() {
        return CODE;
    }
}
