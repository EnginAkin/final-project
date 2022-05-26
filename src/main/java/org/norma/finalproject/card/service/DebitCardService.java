package org.norma.finalproject.card.service;

import org.norma.finalproject.card.entity.DebitCard;

import java.util.List;
import java.util.Optional;

public interface DebitCardService {

    DebitCard save(DebitCard card);

    Optional<DebitCard> findById(long debitCardID);

    List<DebitCard> getAllCustomersDebitCards(long customerID);

    Optional<DebitCard> findDebitCardWithCustomerIDAndCardID(long customerID, long debitCardId);

    Optional<DebitCard> findDebitCardWithCustomerIDAndCardNumber(long customerID, String cardNumber);

    Optional<DebitCard> findDebitCardWithCardNumber(String cardNumber);


    boolean existsDebitCardByCheckingAccountId(long parentCheckingAccountID);
}
