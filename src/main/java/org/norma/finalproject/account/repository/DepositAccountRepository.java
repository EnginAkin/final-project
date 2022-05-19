package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepositAccountRepository extends JpaRepository<DepositAccount, Long> {

    boolean existsDepositAccountByAccountNo(String accountNo);

    boolean existsDepositAccountByAccountNameAndCustomer(String accountName, Customer customer);


    @Query(value = "select case when count(c) > 0 then true else false end from deposit_account c where c.balance > 0 and  c.customer_id=?1", nativeQuery = true)
    boolean existsDepositAccountsBalanceGreatherThanZeroByCustomerId(long CustomerId);

}

