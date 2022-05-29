package org.norma.finalproject.card.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.exception.NotFoundException;

public class CreditCardOperationException extends BusinessException {
    public CreditCardOperationException(String message) {
        super(message);
    }
}
