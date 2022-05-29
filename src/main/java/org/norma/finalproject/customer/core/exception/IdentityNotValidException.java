package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class IdentityNotValidException extends BusinessException {
    public IdentityNotValidException() {
        super("Identity not valid.");
    }
}
