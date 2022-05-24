package org.norma.finalproject.card.service;

import org.norma.finalproject.card.entity.DebitCard;

import java.util.List;
import java.util.Optional;

public interface DebitCardService {

    DebitCard save(DebitCard card);

    Optional<DebitCard> findById(long debitCardID);

    List<DebitCard> getAllCustomersDebitCards(long customerID);
    Optional<DebitCard> getDebitCardWithCustomerIDAndCardID(long customerID,long debitCardId);

    boolean existsDebitCardByCheckingAccountId(long parentCheckingAccountID);
}
