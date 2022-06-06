package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {
    List<CheckingAccount> findAllByCustomer_IdAndBlocked(long customerId, boolean blocked);

    Optional<CheckingAccount> findByAccountNo(String accountNo);

    Optional<CheckingAccount> findFirstByCustomer_Email(String email);


}

