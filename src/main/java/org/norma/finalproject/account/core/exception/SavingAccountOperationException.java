package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class SavingAccountOperationException extends BusinessException {
    public SavingAccountOperationException(String message) {
        super(message);
    }
}
