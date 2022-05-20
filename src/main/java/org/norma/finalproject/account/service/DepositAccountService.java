package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.DepositAccount;

import java.util.Optional;

public interface DepositAccountService {

    boolean checkIsAccountNoUnique(String accountNo);

    DepositAccount save(DepositAccount depositAccount);


    Optional<DepositAccount> findById(long accountId);

    void deleteCustomerDepositAccount(DepositAccount depositAccount);
}
