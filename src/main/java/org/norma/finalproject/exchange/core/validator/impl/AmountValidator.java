package org.norma.finalproject.exchange.core.validator.impl;

import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.core.validator.Validator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AmountValidator implements Validator<BigDecimal> {

    @Override
    public void validate(BigDecimal amount) throws AmountNotValidException {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountNotValidException("Amount Not Valid");
        }
    }
}


