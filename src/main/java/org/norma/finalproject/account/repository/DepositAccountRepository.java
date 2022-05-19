package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DepositAccountRepository extends JpaRepository<DepositAccount,Long> {

    boolean existsDepositAccountByAccountNo(String accountNo);
    boolean existsDepositAccountByAccountNameAndCustomer(String accountName, Customer customer);

}

