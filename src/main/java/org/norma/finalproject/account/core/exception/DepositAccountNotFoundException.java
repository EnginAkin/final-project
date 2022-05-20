package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class DepositAccountNotFoundException extends BusinessException {
    public DepositAccountNotFoundException(String message) {
        super(message);
    }
}
