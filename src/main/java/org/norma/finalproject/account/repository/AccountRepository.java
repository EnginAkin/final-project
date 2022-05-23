package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsAccountByAccountNo(String accountNo);
    boolean existsAccountByIbanNo(String ibanNo);
}
