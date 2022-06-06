package org.norma.finalproject.card.service;

import org.norma.finalproject.card.entity.DebitCard;

import java.util.List;
import java.util.Optional;

public interface DebitCardService {

    DebitCard save(DebitCard card);


    List<DebitCard> getAllCustomersDebitCards(long customerID);

    Optional<DebitCard> findDebitCardWithCustomerIDAndCardID(long customerID, long debitCardId);


    Optional<DebitCard> findDebitCardWithCardNumber(String cardNumber);


    boolean existsDebitCardByCheckingAccountId(long parentCheckingAccountID);

    Optional<DebitCard> findByParentCheckingAccount(long parentId);

    void deleteByCheckingAccountId(long parentId);

    void delete(DebitCard debitCard);
}
