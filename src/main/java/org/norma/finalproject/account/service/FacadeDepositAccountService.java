package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface FacadeDepositAccountService {
    GeneralResponse create(Long customerId, CreateDepositAcoountRequest createDepositAcoountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException;


}
