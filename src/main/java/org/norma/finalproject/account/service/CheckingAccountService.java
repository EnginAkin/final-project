package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.CheckingAccount;

import java.util.List;
import java.util.Optional;

public interface CheckingAccountService {

    CheckingAccount save(CheckingAccount checkingAccount);
    Optional<CheckingAccount> findById(long accountId);
    void deleteCustomerCheckingAccountById(long checkingAccountId);
    List<CheckingAccount> getUnBlockedAccounts(long customerId);
    Optional<CheckingAccount> getAccountByAccountNumber(String parentAccountNumber);

}
