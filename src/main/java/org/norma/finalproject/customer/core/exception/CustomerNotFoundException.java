package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.exception.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException() {
        super("Customer not found.");
    }
}
