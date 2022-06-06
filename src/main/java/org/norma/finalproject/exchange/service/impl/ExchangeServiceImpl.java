package org.norma.finalproject.exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.exchange.core.model.Exchange;
import org.norma.finalproject.exchange.service.ExchangeService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * this is for exchange money
 *
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {
    private final RestTemplate restTemplate;

    @Override
    public Double getExchange(String url, HttpEntity httpEntity) throws HttpClientErrorException {
        ResponseEntity<Exchange> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Exchange.class);
        return exchange.getBody().getResult();
    }


}
