package org.norma.finalproject.exchange.core.exception;


import org.norma.finalproject.common.exception.BusinessException;

public class AmountNotValidException  extends BusinessException {
    public AmountNotValidException(String message) {
        super(message);
    }
}
