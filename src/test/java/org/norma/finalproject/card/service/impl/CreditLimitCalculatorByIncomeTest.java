package org.norma.finalproject.card.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.card.entity.enums.CreditRateType;
import org.norma.finalproject.card.service.CreditRateService;

import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
class CreditLimitCalculatorByIncomeTest {

    @Mock
    private CreditRateService creditRateService;
    @InjectMocks
    private CreditLimitCalculatorByIncome underTest;

    @Test
    public void givenIncomeAndDesiredCreditLimit_whenGetCreditLimit_thenRetunModorateRisky() {
        // given
        BigDecimal income = BigDecimal.valueOf(1000);
        BigDecimal desiredCreditLimit = BigDecimal.valueOf(1000);
        BDDMockito.given(creditRateService.getCreditRate(income)).willReturn(CreditRateType.MODERATE_RISKY);
        // when
        BigDecimal creditLimit = underTest.getCreditLimit(income, desiredCreditLimit);
        //then
        BigDecimal expectedCreditLimit = desiredCreditLimit.multiply(BigDecimal.valueOf(0.5));
        Assertions.assertThat(creditLimit).isEqualTo(expectedCreditLimit);
    }


    @Test
    public void givenIncomeAndDesiredCreditLimit_whenGetCreditLimit_thenRetunVeryRisky() {
        // given
        BigDecimal income = BigDecimal.valueOf(0);
        BigDecimal desiredCreditLimit = BigDecimal.valueOf(1000);
        BDDMockito.given(creditRateService.getCreditRate(income)).willReturn(CreditRateType.RISKY);
        // when
        BigDecimal creditLimit = underTest.getCreditLimit(income, desiredCreditLimit);
        //then
        BigDecimal expectedCreditLimit = BigDecimal.ZERO;
        Assertions.assertThat(creditLimit).isEqualTo(expectedCreditLimit);


    }

    @Test
    public void givenIncomeAndDesiredCreditLimit_whenGetCreditLimit_thenRetunGood() {
        // given
        BigDecimal income = BigDecimal.valueOf(30000);
        BigDecimal desiredCreditLimit = BigDecimal.valueOf(1000);
        BDDMockito.given(creditRateService.getCreditRate(income)).willReturn(CreditRateType.GOOD);
        // when
        BigDecimal creditLimit = underTest.getCreditLimit(income, desiredCreditLimit);
        //then
        BigDecimal expectedCreditLimit = desiredCreditLimit;
        Assertions.assertThat(creditLimit).isEqualTo(expectedCreditLimit);
    }
}