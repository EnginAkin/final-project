package org.norma.finalproject.customer.core.utilities;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

class UtilsTest {


    @Test
    void givenInvalidAge_whenIsOver18YearsOld_thenReturnsFalse() {

        // given
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 3, 15);
        Date date = calendar.getTime();
        // when
        boolean over18YearsOld = Utils.isOver18YearsOld(date);
        //then
        Assertions.assertThat(over18YearsOld).isFalse();


    }

    @Test
    void givenvalidAge_whenIsOver18YearsOld_thenReturnsFalse() {
        // given
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, 3, 15);
        Date date = calendar.getTime();
        // when
        boolean over18YearsOld = Utils.isOver18YearsOld(date);
        //then
        Assertions.assertThat(over18YearsOld).isTrue();


    }

    @Test
    void whenCalculateSuccessRate_thenReturnSuccessRate() {
        //given
        BigDecimal expectedRate = BigDecimal.valueOf(20);
        BigDecimal amoun = BigDecimal.valueOf(20);
        BigDecimal target = BigDecimal.valueOf(100);
        // when
        BigDecimal successRate = Utils.calculateSuccessRate(amoun, target);
        // then
        Assertions.assertThat(successRate).isEqualTo(expectedRate);
    }
}