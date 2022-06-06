package org.norma.finalproject.card.repository;

import org.norma.finalproject.card.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    Optional<CreditCard> findByCardNumber(String cardNumber);

    @Query(value = "from CreditCard c  where day(c.creditCardAccount.cutOffDate)=day(CURRENT_DATE ) AND month(c.creditCardAccount.cutOffDate)=month(CURRENT_DATE ) AND year(c.creditCardAccount.cutOffDate) = year (CURRENT_DATE )")
    List<CreditCard> findAllCutoffDateInToday();
}
