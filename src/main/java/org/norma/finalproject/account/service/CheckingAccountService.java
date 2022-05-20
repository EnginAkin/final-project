package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.CheckingAccount;

import java.util.Optional;

public interface CheckingAccountService {

    boolean checkIsAccountNoUnique(String accountNo);

    CheckingAccount save(CheckingAccount checkingAccount);


    Optional<CheckingAccount> findById(long accountId);

    void deleteCustomerCheckingAccount(CheckingAccount checkingAccount);
}
