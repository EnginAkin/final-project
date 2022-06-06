package org.norma.finalproject.card.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.card.entity.enums.CreditRateType;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class CreditRateServiceImplTest {

    private CreditRateServiceImpl underTest = new CreditRateServiceImpl();

    @Test
    public void givenIncome_whenGetCreditRate_thenReturnRisky() {
        // given
        BigDecimal income = BigDecimal.valueOf(250);
        // when
        CreditRateType creditRate = underTest.getCreditRate(income);
        // then
        CreditRateType expectedRateType = CreditRateType.RISKY;
        Assertions.assertThat(creditRate).isEqualTo(expectedRateType);

    }

    @Test
    public void givenIncome_whenGetCreditRate_thenReturnModorateRisky() {
        // given
        BigDecimal income = BigDecimal.valueOf(750);
        // when
        CreditRateType creditRate = underTest.getCreditRate(income);
        // then
        CreditRateType expectedRateType = CreditRateType.MODERATE_RISKY;
        Assertions.assertThat(creditRate).isEqualTo(expectedRateType);

    }

    @Test
    public void givenIncome_whenGetCreditRate_thenReturnOddsOnRisky() {
        // given
        BigDecimal income = BigDecimal.valueOf(2500);
        // when
        CreditRateType creditRate = underTest.getCreditRate(income);
        // then
        CreditRateType expectedRateType = CreditRateType.ODDS_ON_RISKY;
        Assertions.assertThat(creditRate).isEqualTo(expectedRateType);

    }

    @Test
    public void givenIncome_whenGetCreditRate_thenReturnGood() {
        // given
        BigDecimal income = BigDecimal.valueOf(5000);
        // when
        CreditRateType creditRate = underTest.getCreditRate(income);
        // then
        CreditRateType expectedRateType = CreditRateType.GOOD;
        Assertions.assertThat(creditRate).isEqualTo(expectedRateType);

    }

    @Test
    public void givenIncome_whenGetCreditRate_thenReturnVeryGood() {
        // given
        BigDecimal income = BigDecimal.valueOf(10000);
        // when
        CreditRateType creditRate = underTest.getCreditRate(income);
        // then
        CreditRateType expectedRateType = CreditRateType.VERY_GOOD;
        Assertions.assertThat(creditRate).isEqualTo(expectedRateType);

    }
}