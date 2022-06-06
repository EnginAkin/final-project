package org.norma.finalproject.exchange.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.exchange.core.constant.ExchangeConstant;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.core.validator.Validator;
import org.norma.finalproject.exchange.service.ExchangeService;
import org.norma.finalproject.exchange.service.impl.FacadeExchangeServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class FacadeExchangeServiceImplTest {

    @Mock
    private ExchangeService exchangeService;
    @Mock
    private Validator<BigDecimal> amountValidator;

    @InjectMocks
    private FacadeExchangeServiceImpl facadeExchangeService;


    @Test
    void givenValidExchangeParameter_whenGetExchangedAmount_thenReturnExchangedAmount() throws AmountNotValidException, AmountNotValidException {
        // given
        CurrencyType from = CurrencyType.USD;
        CurrencyType to = CurrencyType.TRY;
        BigDecimal amount = BigDecimal.valueOf(1);
        doNothing().when(amountValidator).validate(amount);
        String url = ExchangeConstant.API_LAYER_URL + "/convert?to=" + to + "&from=" + from + "&amount=" + amount;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", ExchangeConstant.API_LAYER_KEY);
        Double expectedExchangeAmount = 15D;
        HttpEntity httpEntity = new HttpEntity(headers);
        BDDMockito.given(exchangeService.getExchange(url, httpEntity)).willReturn(expectedExchangeAmount);
        // when
        BigDecimal exchangedAmount = facadeExchangeService.getExchangedAmount(to, from, amount);
        // then  verifiy
        assertThat(exchangedAmount).isEqualTo(BigDecimal.valueOf(expectedExchangeAmount));

    }

    @Test
    void givenInvalidExchangeParameter_whenGetExchangedAmount_thenReturnExchangedAmount() throws AmountNotValidException {
        // given
        CurrencyType from = CurrencyType.USD;
        CurrencyType to = CurrencyType.TRY;
        BigDecimal invalidAmount = BigDecimal.valueOf(-1);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        String url = ExchangeConstant.API_LAYER_URL + "/convert?to=" + to + "&from=" + from + "&amount=" + invalidAmount;
        doThrow(AmountNotValidException.class)
                .when(amountValidator)
                .validate(invalidAmount);
        // when
        assertThrows(AmountNotValidException.class, () -> {
            facadeExchangeService.getExchangedAmount(to, from, invalidAmount);
        });
        // then
        Mockito.verify(exchangeService, Mockito.atMost(0)).getExchange(url, httpEntity);

    }


}