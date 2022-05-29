package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.core.exception.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException() {
        super("Customer not found.");
    }
}
