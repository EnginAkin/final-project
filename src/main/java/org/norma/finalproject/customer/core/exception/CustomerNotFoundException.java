package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class CustomerNotFoundException extends BusinessException {
    public CustomerNotFoundException() {
        super("Customer not found.");
    }
}
