package org.norma.finalproject.card.core.mapper;

import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;

public interface DebitCardMapper {

    DebitCardResponse toDto(DebitCard debitCard);
}
