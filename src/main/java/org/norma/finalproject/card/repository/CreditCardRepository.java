package org.norma.finalproject.card.repository;

import org.norma.finalproject.card.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard , Long> {

    Optional<CreditCard> findByCardNumber(String cardNumber);
}
