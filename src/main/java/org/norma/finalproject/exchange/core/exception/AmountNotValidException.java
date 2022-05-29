package org.norma.finalproject.exchange.core.exception;


import org.norma.finalproject.common.core.exception.BusinessException;

public class AmountNotValidException extends BusinessException {
    public AmountNotValidException(String message) {
        super(message);
    }
}
