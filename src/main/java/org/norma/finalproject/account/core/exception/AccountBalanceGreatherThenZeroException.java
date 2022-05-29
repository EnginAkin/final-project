package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class AccountBalanceGreatherThenZeroException extends BusinessException {
    public AccountBalanceGreatherThenZeroException(String message) {
        super(message);
    }
}
