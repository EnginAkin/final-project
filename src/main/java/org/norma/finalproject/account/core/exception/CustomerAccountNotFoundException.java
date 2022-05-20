package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class CustomerAccountNotFoundException extends BusinessException {
    public CustomerAccountNotFoundException(String message) {
        super(message);
    }
}
