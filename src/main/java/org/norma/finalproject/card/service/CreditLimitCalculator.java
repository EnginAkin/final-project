package org.norma.finalproject.card.service;

import java.math.BigDecimal;

public interface CreditLimitCalculator<T> {

    BigDecimal getCreditLimit(T data, BigDecimal desiredCreditLimit);

}
