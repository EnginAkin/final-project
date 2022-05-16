package org.norma.finalproject.exchange.service;

import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.springframework.web.client.HttpClientErrorException;

public interface FacadeExchangeService {

    GeneralDataResponse getExchangedAmount(String to, String from, int amount) throws HttpClientErrorException, AmountNotValidException;
}
