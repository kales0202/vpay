package com.synway.vpay.exception;

import com.synway.vpay.base.exception.BusinessException;

/**
 * 补单失败异常
 *
 * @since 0.1
 */
public class UnimportantException extends BusinessException {
    public static final int CODE = -7;

    public static final String MESSAGE = "不重要异常...";

    public UnimportantException() {
        super(MESSAGE);
    }

    public UnimportantException(String message) {
        super(message);
    }

    public UnimportantException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnimportantException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getCode() {
        return CODE;
    }
}
