package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.SavingAccount;

import java.util.List;
import java.util.Optional;

public interface SavingAccountService {

    SavingAccount save(SavingAccount savingAccount);


    boolean isUsedParentAccountForSavingAccount(long customerId, long parentCheckingAccountId);

    List<SavingAccount> getAllAccountsByCustomerId(Long customerId);
    Optional<SavingAccount> getByParentId(Long parentAccountID);

}
