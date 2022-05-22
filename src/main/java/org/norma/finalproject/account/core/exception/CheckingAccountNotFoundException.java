package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class CheckingAccountNotFoundException extends BusinessException {
    public CheckingAccountNotFoundException(String message) {
        super(message);
    }
}
