package com.synway.vpay.exception;

import com.synway.vpay.base.exception.BusinessException;

/**
 * 补单失败异常
 *
 * @since 0.1
 */
public class FulfillException extends BusinessException {
    public static final int CODE = -5;

    public static final String MESSAGE = "补单失败...";

    public FulfillException() {
        super(MESSAGE);
    }

    public FulfillException(String message) {
        super(message);
    }

    public FulfillException(String message, Throwable cause) {
        super(message, cause);
    }

    public FulfillException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getCode() {
        return CODE;
    }
}
