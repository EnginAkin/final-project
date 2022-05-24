package org.norma.finalproject.card.repository;

import org.norma.finalproject.card.entity.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebitCardRepository extends JpaRepository<DebitCard,Long> {

    List<DebitCard> findAllByCheckingAccount_Customer_Id(long id);
}
