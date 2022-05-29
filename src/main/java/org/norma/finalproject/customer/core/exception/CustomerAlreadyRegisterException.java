package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class CustomerAlreadyRegisterException extends BusinessException {
    public CustomerAlreadyRegisterException() {
        super("Customer already registered.");
    }
}
