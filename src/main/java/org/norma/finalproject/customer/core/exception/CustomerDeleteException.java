package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class CustomerDeleteException extends BusinessException {
    public CustomerDeleteException(String message) {
        super(message);
    }
}
