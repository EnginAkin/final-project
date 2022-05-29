package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class AccountNameAlreadyHaveException extends BusinessException {
    public AccountNameAlreadyHaveException(String message) {
        super(message);
    }
}
