package org.norma.finalproject.exchange.core.validator;


import org.norma.finalproject.exchange.core.exception.AmountNotValidException;

public interface Validator<T> {
    void validate(T data) throws AmountNotValidException;
}
