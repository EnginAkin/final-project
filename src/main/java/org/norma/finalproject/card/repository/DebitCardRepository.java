package org.norma.finalproject.card.repository;

import org.norma.finalproject.card.entity.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DebitCardRepository extends JpaRepository<DebitCard,Long> {

    List<DebitCard> findAllByCheckingAccount_Customer_Id(long id);
    Optional<DebitCard> findDebitCardByCheckingAccount_Customer_IdAndId(long customerID, long debitCardId);
    Optional<DebitCard> findDebitCardByCheckingAccount_Customer_IdAndCardNumber(long customerID, String cardNumber);

    boolean existsByCheckingAccount_Id(Long checkingAccountID);
}
