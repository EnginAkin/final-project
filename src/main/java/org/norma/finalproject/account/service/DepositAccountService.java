package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.customer.entity.Customer;

public interface DepositAccountService {

    boolean checkIsAccountNoUnique(String accountNo);

    DepositAccount save(DepositAccount depositAccount);

    boolean existsCustomerDepositAccountByAccountName(Long customerId, String accountName);

    boolean checkCustomerHasMoneyInDepositAccounts(long id);

    boolean checkCustomerHasMoneyInDepositAccountByAccountName(Long customerId, String accountName);

    void deleteCustomerDepositAccount(Long customerId, String accountName);
}
