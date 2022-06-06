package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {
    boolean existsByParentAccount_IdAndCustomer_Id(long parentAccountId, long customerId);

    List<SavingAccount> findAllByCustomer_Id(long customerId);

    // AND month(s.maturityDate)=month(CURRENT_DATE ) AND year(s.maturityDate) = year (CURRENT_DATE )
    @Query(value = "from SavingAccount s  where day(s.maturityDate)=day(CURRENT_DATE ) AND month(s.maturityDate)=month(CURRENT_DATE ) AND year(s.maturityDate) = year (CURRENT_DATE )")
    List<SavingAccount> getAllSavingAccountMaturityDateInToday();


    Optional<SavingAccount> findSavingAccountByParentAccount_Id(long parentAccountId);

}
