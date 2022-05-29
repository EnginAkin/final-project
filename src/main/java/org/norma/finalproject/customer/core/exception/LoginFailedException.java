package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class LoginFailedException extends BusinessException {
    public LoginFailedException() {
        super("Login failed");
    }
}
