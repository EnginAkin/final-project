package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.customer.entity.Customer;

import java.util.List;

public interface SavingAccountService {

    SavingAccount save(SavingAccount savingAccount);


    boolean isUsedParentAccountForSavingAccount(long customerId, long parentCheckingAccountId);

    List<SavingAccount> getAllAccountsByCustomerId(Long customerId);

}
