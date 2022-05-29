package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.core.exception.NotFoundException;

public class CheckingAccountNotFoundException extends NotFoundException {
    public CheckingAccountNotFoundException(String message) {
        super(message);
    }
}
