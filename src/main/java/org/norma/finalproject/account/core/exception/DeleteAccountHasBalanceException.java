package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class DeleteAccountHasBalanceException extends BusinessException {
    public DeleteAccountHasBalanceException(String message) {
        super(message);
    }
}
