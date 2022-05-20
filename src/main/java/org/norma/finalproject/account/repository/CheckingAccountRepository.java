package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {

    boolean existsCheckingAccountByAccountNo(String accountNo);

    boolean existsCheckingAccountByAccountNameAndCustomer_Id(String accountName, Long customerId);


    @Query(value = "select case when count(c) > 0 then true else false end from deposit_account c where c.balance > 0 and  c.customer_id=?1", nativeQuery = true)
    boolean existsCheckingAccountsBalanceGreatherThanZeroByCustomerId(long CustomerId);

    @Query(value = "select case when count(c) > 0 then true else false end from deposit_account c where c.balance > 0 and  c.customer_id=?1 and c.account_name=?2", nativeQuery = true)
    boolean existsCheckingAccountBalanceGreatherThanZeroByAccountNameAndCustomerId(long CustomerId, String accountName);


    CheckingAccount findByAccountNameAndCustomer_Id(String accountName, long customerId);
}

