package org.norma.finalproject.card.core.mapper;

import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;

public interface CreditCardMapper {
    CreditCardResponse toCreditCardResponse(CreditCard creditCard);

}
