package org.norma.finalproject.exchange.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.exchange.core.model.Exchange;
import org.norma.finalproject.exchange.service.impl.ExchangeServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    @Test
    void givenValidUrl_whenGetExchangeAmount_thenReturnExchangedAmount() {
        // given
        String url = "valid url";
        HttpHeaders headers = new HttpHeaders();
        headers.set("key", "key");
        HttpEntity httpEntity = new HttpEntity(headers);
        Double expectedAmount = 15d;
        Exchange exchange = new Exchange();
        exchange.setResult(expectedAmount);
        BDDMockito.given(restTemplate.exchange(url, HttpMethod.GET, httpEntity, Exchange.class)).willReturn(ResponseEntity.of(Optional.of(exchange)));
        // when
        Double exchangedAmount = exchangeService.getExchange(url, httpEntity);
        // then
        Assertions.assertThat(exchangedAmount).isEqualTo(exchangedAmount);
    }

    @Test
    void givenInvalidUrl_whenGetExchangeAmount_thenReturnExchangedAmount() {

        // given
        String url = "invalid url";
        HttpHeaders headers = new HttpHeaders();
        headers.set("key", "key");
        HttpEntity httpEntity = new HttpEntity(headers);
        Double expectedAmount = 15d;
        Exchange exchange = new Exchange();
        exchange.setResult(expectedAmount);
        BDDMockito.given(restTemplate.exchange(url, HttpMethod.GET, httpEntity, Exchange.class)).willThrow(HttpClientErrorException.class);
        // when - then
        assertThrows(HttpClientErrorException.class, () -> {
            exchangeService.getExchange(url, httpEntity);
        });


    }
}