package org.norma.finalproject.card.core.mapper;

import org.norma.finalproject.card.core.model.response.CreditCardActivityResponse;
import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.base.CreditCardActivity;

public interface CreditCardMapper {
    CreditCardResponse toCreditCardActivityResponse(CreditCard creditCard);

    CreditCardActivityResponse toCreditCardActivityResponse(CreditCardActivity activity);

}
