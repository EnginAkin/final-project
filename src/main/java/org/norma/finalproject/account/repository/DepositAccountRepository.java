package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepositAccountRepository extends JpaRepository<DepositAccount, Long> {

    boolean existsDepositAccountByAccountNo(String accountNo);

    boolean existsDepositAccountByAccountNameAndCustomer_Id(String accountName, Long customerId);


    @Query(value = "select case when count(c) > 0 then true else false end from deposit_account c where c.balance > 0 and  c.customer_id=?1", nativeQuery = true)
    boolean existsDepositAccountsBalanceGreatherThanZeroByCustomerId(long CustomerId);

    @Query(value = "select case when count(c) > 0 then true else false end from deposit_account c where c.balance > 0 and  c.customer_id=?1 and c.account_name=?2", nativeQuery = true)
    boolean existsDepositAccountBalanceGreatherThanZeroByAccountNameAndCustomerId(long CustomerId,String accountName);


    DepositAccount findByAccountNameAndCustomer_Id(String accountName,long customerId);
}

