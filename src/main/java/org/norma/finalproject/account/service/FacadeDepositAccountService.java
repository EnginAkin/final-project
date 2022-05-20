package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.core.exception.CustomerAccountNotFoundException;
import org.norma.finalproject.account.core.exception.DeleteAccountHasBalanceException;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;

public interface FacadeDepositAccountService {

    GeneralResponse delete(Customer customer, String accountName) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException;

    GeneralResponse create(Customer customer, CreateDepositAcoountRequest createDepositAcoountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException;
}
