package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.exception.NotFoundException;

public class DepositAccountNotFoundException extends NotFoundException {
    public DepositAccountNotFoundException(String message) {
        super(message);
    }
}
