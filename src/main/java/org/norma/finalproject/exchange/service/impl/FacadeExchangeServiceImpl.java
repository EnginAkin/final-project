package org.norma.finalproject.exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.exchange.core.constant.ExchangeConstant;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.core.validator.Validator;
import org.norma.finalproject.exchange.service.ExchangeService;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
@Service
@RequiredArgsConstructor
public class FacadeExchangeServiceImpl implements FacadeExchangeService {

    private final ExchangeService exchangeService;
    private final Validator<Integer> amountValidator;
    @Override
    public GeneralDataResponse getExchangedAmount(String to, String from, int amount) throws AmountNotValidException {
        amountValidator.validate(amount);
        String url= ExchangeConstant.API_LAYER_URL +"/convert?to="+to+"&from="+from+"&amount="+amount;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey",ExchangeConstant.API_LAYER_KEY);
        Double exchangedAmount = exchangeService.getExchange(url, new HttpEntity<>(headers));
        return new GeneralDataResponse<>(exchangedAmount);
    }
}
