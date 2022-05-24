package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.AccountActivity;

import java.util.List;
import java.util.Optional;

public interface CheckingAccountService {

    CheckingAccount save(CheckingAccount checkingAccount);
    Optional<CheckingAccount> findById(long accountId);
    void deleteCustomerCheckingAccountById(CheckingAccount checkingAccount);
    List<CheckingAccount> getUnBlockedAccounts(long customerId);
    Optional<CheckingAccount> getAccountByAccountNumber(String parentAccountNumber);

}
