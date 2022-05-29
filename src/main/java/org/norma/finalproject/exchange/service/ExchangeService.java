package org.norma.finalproject.exchange.service;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;

public interface ExchangeService {

    Double getExchange(String url, HttpEntity entity) throws HttpClientErrorException;
}
