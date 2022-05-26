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
    public BigDecimal getCreditLimit(BigDecimal income) {
        CreditRateType creditRate = creditRateService.getCreditRate(income);
        if (creditRate.equals(CreditRateType.VERY_GOOD)) {
            return BigDecimal.valueOf(20000);
        } else if (creditRate.equals(CreditRateType.GOOD)) {
            return BigDecimal.valueOf(8000);

        } else if (creditRate.equals(CreditRateType.ODDS_ON_RISKY)) {
            return BigDecimal.valueOf(4000);

        } else if (creditRate.equals(CreditRateType.MODERATE_RISKY)) {
            return BigDecimal.valueOf(2000);

        }
        return BigDecimal.valueOf(1000);

    }
}
