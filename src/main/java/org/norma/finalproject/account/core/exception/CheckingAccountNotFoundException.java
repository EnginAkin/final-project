package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.exception.NotFoundException;

public class CheckingAccountNotFoundException extends NotFoundException {
    public CheckingAccountNotFoundException(String message) {
        super(message);
    }
}
