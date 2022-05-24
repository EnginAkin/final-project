package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountActivityRepository extends JpaRepository<AccountActivity,Long> {
    List<AccountActivity> findAllByAccount_IdAndAccount_Customer_IdAndAccount_AccountType(long accountID, long customerID, AccountType accountType);
}
