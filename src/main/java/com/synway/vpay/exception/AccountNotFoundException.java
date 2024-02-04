package com.synway.vpay.exception;

import com.synway.vpay.base.exception.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public static final String MESSAGE = "账号不存在...";

    public AccountNotFoundException() {
        super(MESSAGE);
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
