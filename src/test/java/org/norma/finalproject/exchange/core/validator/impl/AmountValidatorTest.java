package org.norma.finalproject.exchange.core.validator.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AmountValidatorTest {


    @Mock
    private AmountValidator amountValidator;

    @Test
    public void givenValidAmount_whenValidate() {
        // given
        int validAmount = 1;
        // verify
        assertDoesNotThrow(() -> {
            amountValidator.validate(validAmount);
        });
    }

    @Test
    public void givenInvalidAmount_whenValidate_thenThrowsAmountNotValidException() throws AmountNotValidException {
        // given pre condition
        int invalidAmount = -1;
        // when
        doThrow(AmountNotValidException.class)
                .when(amountValidator)
                .validate(invalidAmount);
        // then vefify
        assertThrows(AmountNotValidException.class, () -> {
            amountValidator.validate(invalidAmount);
        });
    }

}