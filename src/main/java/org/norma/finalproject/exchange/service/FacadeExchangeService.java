package org.norma.finalproject.exchange.service;

import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

public interface FacadeExchangeService {

    BigDecimal getExchangedAmount(CurrencyType to, CurrencyType from, BigDecimal amount) throws HttpClientErrorException, AmountNotValidException;
}
