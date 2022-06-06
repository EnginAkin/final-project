package org.norma.finalproject.card.service;

import org.norma.finalproject.card.entity.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    CreditCard save(CreditCard creditCard);

    Optional<CreditCard> findCreditCardByCreditCardNumber(String cardNumber);

    Optional<CreditCard> findCreditCardById(long id);

    void delete(CreditCard creditCard);

    List<CreditCard> findAllCreditCardsCutoffDateInToday();
}
