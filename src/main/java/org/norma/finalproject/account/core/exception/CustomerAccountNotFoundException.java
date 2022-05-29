package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.core.exception.NotFoundException;

public class CustomerAccountNotFoundException extends NotFoundException {
    public CustomerAccountNotFoundException(String message) {
        super(message);
    }
}
