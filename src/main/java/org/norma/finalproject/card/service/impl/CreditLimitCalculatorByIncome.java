package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.entity.enums.CreditRateType;
import org.norma.finalproject.card.service.CreditLimitCalculator;
import org.norma.finalproject.card.service.CreditRateService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CreditLimitCalculatorByIncome<T> implements CreditLimitCalculator<BigDecimal> {
    private final CreditRateService creditRateService;

    @Override
    public BigDecimal getCreditLimit(BigDecimal income,BigDecimal desiredCreditLimit) {
        CreditRateType creditRate = creditRateService.getCreditRate(income);
        if (creditRate.equals(CreditRateType.VERY_GOOD)) {
            return desiredCreditLimit;
        } else if (creditRate.equals(CreditRateType.GOOD)) {
            return desiredCreditLimit;

        } else if (creditRate.equals(CreditRateType.ODDS_ON_RISKY)) {
            return desiredCreditLimit.multiply(BigDecimal.valueOf(0.8));

        } else if (creditRate.equals(CreditRateType.MODERATE_RISKY)) {
            return desiredCreditLimit.multiply(BigDecimal.valueOf(0.5));

        }
         return BigDecimal.ZERO; // very risk not given credit limit

    }
}
