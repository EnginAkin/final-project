package org.norma.finalproject.card.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class DebitOperationException extends BusinessException {
    public DebitOperationException(String message) {
        super(message);
    }
}
