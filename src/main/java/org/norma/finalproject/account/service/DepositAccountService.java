package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.customer.entity.Customer;

public interface DepositAccountService {

    boolean checkIsAccountNoUnique(String accountNo);

    DepositAccount save(DepositAccount depositAccount);


    boolean checkCustomerHasMoneyInDepositAccounts(long id);


    void deleteCustomerDepositAccount(DepositAccount depositAccount);
}
