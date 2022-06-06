package org.norma.finalproject.card.repository;

import org.norma.finalproject.card.entity.CreditCardAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardAccountRepository extends JpaRepository<CreditCardAccount, Long> {
}
