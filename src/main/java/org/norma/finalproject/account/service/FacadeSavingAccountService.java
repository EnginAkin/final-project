package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.exception.SavingAccountNotFound;
import org.norma.finalproject.account.core.exception.SavingAccountOperationException;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;

public interface FacadeSavingAccountService {
    GeneralResponse create(Long customerID, CreateSavingAccountRequest createSavingAccountRequest) throws CustomerNotFoundException, CheckingAccountNotFoundException, SavingAccountOperationException, AmountNotValidException, TransferOperationException;

    GeneralResponse getAccounts(Long customerID) throws CustomerNotFoundException;

    GeneralResponse getAccountActivities(Long customerID, long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException, ActivitiesNotFoundException, SavingAccountOperationException;

    void deleteByCheckingParentId(Long checkingId) throws SavingAccountNotFound;
}
