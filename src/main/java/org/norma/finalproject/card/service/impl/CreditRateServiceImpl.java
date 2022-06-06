package org.norma.finalproject.card.service.impl;

import org.norma.finalproject.card.entity.enums.CreditRateType;
import org.norma.finalproject.card.service.CreditRateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
public class CreditRateServiceImpl implements CreditRateService {
    @Override
    public CreditRateType getCreditRate(BigDecimal income) {
        // credit score calculated according to income
        BigDecimal risky = BigDecimal.valueOf(500);
        BigDecimal moderateRisky = BigDecimal.valueOf(1000);
        BigDecimal oddsOnRisky = BigDecimal.valueOf(4250);
        BigDecimal good = BigDecimal.valueOf(8000);

        if (income.compareTo(risky) <= 0) {
            return CreditRateType.RISKY;
        } else if (income.compareTo(risky) >= 0 && income.compareTo(moderateRisky) <= 0) {
            return CreditRateType.MODERATE_RISKY;
        } else if (income.compareTo(moderateRisky) >= 0 && income.compareTo(oddsOnRisky) <= 0) {
            return CreditRateType.ODDS_ON_RISKY;
        } else if (income.compareTo(oddsOnRisky) >= 0 && income.compareTo(good) <= 0) {
            return CreditRateType.GOOD;
        }
        return CreditRateType.VERY_GOOD;
    }
}
