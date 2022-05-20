package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.*;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;

public interface FacadeDepositAccountService {

    GeneralResponse delete(Customer customer, String accountName) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException;

    GeneralResponse create(Customer customer, CreateDepositAcoountRequest createDepositAcoountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException;

    GeneralResponse blockAccount(long accountId) throws DepositAccountNotFoundException;
}
