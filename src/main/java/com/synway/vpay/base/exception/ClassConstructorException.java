package com.synway.vpay.base.exception;

public class ClassConstructorException extends BusinessException {

    public static final int CODE = -8;
    public static final String MESSAGE = "错误的构造函数...";

    public ClassConstructorException() {
        super(MESSAGE);
    }

    public ClassConstructorException(String message) {
        super(message);
    }

    public ClassConstructorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassConstructorException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getCode() {
        return CODE;
    }
}
