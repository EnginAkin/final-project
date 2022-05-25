package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount,Long> {
    boolean existsByParentAccount_IdAndCustomer_Id(long parentAccountId, long customerId);
    List<SavingAccount> findAllByCustomer_Id(long customerId);
    Optional<SavingAccount> findSavingAccountByParentAccount_Id(long parentAccountId);
}
