package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.AccountBalanceGreatherThenZeroException;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.exception.SavingAccountNotFound;
import org.norma.finalproject.account.core.exception.SavingAccountOperationException;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;

public interface FacadeSavingAccountService {
    GeneralResponse create(Long customerID, CreateSavingAccountRequest createSavingAccountRequest) throws CustomerNotFoundException, CheckingAccountNotFoundException, SavingAccountOperationException, AmountNotValidException, TransferOperationException, DebitCardNotFoundException;

    GeneralResponse getAccounts(Long customerID) throws CustomerNotFoundException;

    GeneralResponse getAccountActivities(Long customerID, long accountID, ActivityFilter filter) throws CustomerNotFoundException, CheckingAccountNotFoundException, ActivitiesNotFoundException, SavingAccountOperationException, SavingAccountNotFound;

    GeneralResponse deleteById(Long customerID, long accountID) throws SavingAccountNotFound, AccountBalanceGreatherThenZeroException;

    GeneralResponse getAccountByAccountID(Long customerID, long accountID) throws CustomerNotFoundException, SavingAccountNotFound;
}
