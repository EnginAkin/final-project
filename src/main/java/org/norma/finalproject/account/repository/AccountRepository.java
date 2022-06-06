package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.base.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsAccountByAccountNo(String accountNo);

    boolean existsAccountByIbanNo(String ibanNo);

    Optional<Account> findByIbanNo(String ibanNo);
}
