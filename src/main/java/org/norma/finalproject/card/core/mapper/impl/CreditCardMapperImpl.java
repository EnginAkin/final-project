package org.norma.finalproject.card.core.mapper.impl;

import org.norma.finalproject.card.core.mapper.CreditCardMapper;
import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMapperImpl implements CreditCardMapper {
    @Override
    public CreditCardResponse toCreditCardResponse(CreditCard creditCard) {
        CreditCardResponse creditCardResponse=new CreditCardResponse();
        creditCardResponse.setCardNumber(creditCard.getCardNumber());
        creditCardResponse.setExpiryDate(creditCard.getExpiryDate());
        creditCardResponse.setCutOffDate(creditCard.getCreditCardAccount().getCutOffDate());
        creditCardResponse.setPaymentDate(creditCard.getCreditCardAccount().getPaymentDate());
        creditCardResponse.setId(creditCard.getId());
        return creditCardResponse;

    }
}
