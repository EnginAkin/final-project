package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.*;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;

public interface FacadeCheckinAccountService {

    GeneralResponse delete(long customerId, String accountName) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException;

    GeneralResponse create(long customerId, CreateCheckingAccountRequest createCheckingAccountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException;

    GeneralResponse blockAccount(long accountId) throws DepositAccountNotFoundException;

    GeneralResponse getAccounts(long id) throws CustomerNotFoundException;
}
