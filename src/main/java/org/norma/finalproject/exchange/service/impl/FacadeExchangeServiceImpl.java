package org.norma.finalproject.exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.exchange.core.constant.ExchangeConstant;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.core.validator.Validator;
import org.norma.finalproject.exchange.service.ExchangeService;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * this is facade exchange money
 *
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
public class FacadeExchangeServiceImpl implements FacadeExchangeService {

    private final ExchangeService exchangeService;
    private final Validator<BigDecimal> amountValidator;

    @Override
    public BigDecimal getExchangedAmount(CurrencyType to, CurrencyType from, BigDecimal amount) throws AmountNotValidException {
        amountValidator.validate(amount);
        if (to.equals(from)) {
            return amount; // if same currency return same amount
        }
        String url = ExchangeConstant.API_LAYER_URL + "/convert?to=" + to + "&from=" + from + "&amount=" + amount;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", ExchangeConstant.API_LAYER_KEY);
        Double exchangedAmount = exchangeService.getExchange(url, new HttpEntity<>(headers));
        return BigDecimal.valueOf(exchangedAmount);
    }
}
