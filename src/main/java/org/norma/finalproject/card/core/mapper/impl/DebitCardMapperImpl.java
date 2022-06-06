package org.norma.finalproject.card.core.mapper.impl;

import org.norma.finalproject.card.core.mapper.DebitCardMapper;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.springframework.stereotype.Component;

@Component
public class DebitCardMapperImpl implements DebitCardMapper {
    @Override
    public DebitCardResponse toDto(DebitCard debitCard) {
        DebitCardResponse response = new DebitCardResponse();
        response.setCardNumber(debitCard.getCardNumber());
        response.setCvv(debitCard.getCvv());
        response.setBalance(debitCard.getCheckingAccount().getBalance());
        response.setExpiryDate(debitCard.getExpiryDate());
        response.setPassword(debitCard.getPassword());
        response.setId(debitCard.getId());
        return response;
    }
}
