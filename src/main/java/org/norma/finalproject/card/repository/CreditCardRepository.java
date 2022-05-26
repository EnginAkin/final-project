package org.norma.finalproject.card.repository;

import org.norma.finalproject.card.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard , Long> {
}
