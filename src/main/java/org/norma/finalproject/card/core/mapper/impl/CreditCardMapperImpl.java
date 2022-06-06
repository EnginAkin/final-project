package org.norma.finalproject.card.core.mapper.impl;

import org.norma.finalproject.card.core.mapper.CreditCardMapper;
import org.norma.finalproject.card.core.model.response.CreditCardActivityResponse;
import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.base.CreditCardActivity;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMapperImpl implements CreditCardMapper {
    @Override
    public CreditCardResponse toCreditCardActivityResponse(CreditCard creditCard) {
        CreditCardResponse creditCardResponse = new CreditCardResponse();
        creditCardResponse.setCardNumber(creditCard.getCardNumber());
        creditCardResponse.setPassword(creditCard.getPassword());
        creditCardResponse.setAvailableBalance(creditCard.getCreditCardAccount().getAvailableBalance());
        creditCardResponse.setCreditLimit(creditCard.getCreditCardAccount().getTotalCreditLimit());
        creditCardResponse.setExpiryDate(creditCard.getExpiryDate());
        creditCardResponse.setCutOffDate(creditCard.getCreditCardAccount().getCutOffDate());
        creditCardResponse.setCvv(creditCard.getCvv());
        creditCardResponse.setPaymentDate(creditCard.getCreditCardAccount().getPaymentDate());
        creditCardResponse.setCustomerName(creditCard.getCustomer().getName());
        creditCardResponse.setCustomerSurname(creditCard.getCustomer().getSurname());
        creditCardResponse.setId(creditCard.getId());
        return creditCardResponse;

    }

    @Override
    public CreditCardActivityResponse toCreditCardActivityResponse(CreditCardActivity activity) {
        CreditCardActivityResponse response = new CreditCardActivityResponse();
        response.setAmount(activity.getAmount());
        response.setCrossAccount(activity.getCrossAccount());
        response.setId(activity.getId());
        response.setDescription(activity.getDescription());
        response.setSpendCategory(activity.getSpendCategory());
        response.setProcessDate(activity.getProcessDate());
        return response;
    }
}
