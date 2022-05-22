package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.exception.SavingAccountOperationException;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface FacadeSavingAccountService {

    GeneralResponse create(Long customerId, CreateSavingAccountRequest createSavingAccountRequest) throws CustomerNotFoundException, CheckingAccountNotFoundException, SavingAccountOperationException;

    GeneralResponse getAccounts(Long id) throws CustomerNotFoundException;
}
