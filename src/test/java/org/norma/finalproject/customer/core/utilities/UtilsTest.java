package org.norma.finalproject.customer.core.utilities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {



    @Test
    void givenValidAge_whenIsOver18YearsOld_thenReturnBoolean() {
        Calendar calendar=Calendar.getInstance();
        calendar.set(2000, 3, 19);
        boolean over18YearsOld = Utils.isOver18YearsOld(calendar.getTime());
        System.out.println("calendat "+ calendar.getTime());

        assertThat(over18YearsOld).isTrue();

    }

    @Test
    void givenInvalidAge_whenIsOver18YearsOld_thenReturnFalse() {
        Calendar calendar=Calendar.getInstance();
        calendar.set(2006, 3, 19);
        boolean over18YearsOld = Utils.isOver18YearsOld(calendar.getTime());
        System.out.println("calendat "+ calendar.getTime());

        assertThat(over18YearsOld).isFalse();

    }
}