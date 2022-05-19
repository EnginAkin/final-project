package org.norma.finalproject.exchange.core.validator.impl;

import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.core.validator.Validator;
import org.springframework.stereotype.Service;

@Service
public class AmountValidator implements Validator<Integer> {
    @Override
    public void validate(Integer amount) throws AmountNotValidException {
        if (amount < 0) {
            throw new AmountNotValidException("Amount Not Valid");
        }
    }
}
