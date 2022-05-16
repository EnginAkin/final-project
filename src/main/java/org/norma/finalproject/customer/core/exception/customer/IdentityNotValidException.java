package org.norma.finalproject.customer.core.exception.customer;

import org.norma.finalproject.common.exception.BusinessException;

public class IdentityNotValidException extends BusinessException {
    public IdentityNotValidException() {
        super("Identity not valid.");
    }
}
