package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class UpdateCustomerSamePasswordException extends BusinessException {
    public UpdateCustomerSamePasswordException() {
        super("Cannot be the same as the old password.");
    }
}
