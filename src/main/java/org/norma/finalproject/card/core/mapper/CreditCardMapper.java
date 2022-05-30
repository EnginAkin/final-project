package org.norma.finalproject.card.core.mapper;

import org.norma.finalproject.card.core.model.response.CreditCardActivityResponse;
import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.base.CreditCardActivity;

public interface CreditCardMapper {
    CreditCardResponse toCreditCardResponse(CreditCard creditCard);
    CreditCardActivityResponse toCreditCardResponse(CreditCardActivity activity);

}
