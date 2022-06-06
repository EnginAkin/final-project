package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.exception.*;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface FacadeCheckinAccountService {

    GeneralResponse deleteById(long customerID, long accountID) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException, SavingAccountNotFound, AccountBalanceGreatherThenZeroException, CheckingAccountNotFoundException;

    GeneralResponse create(long customerID, CreateCheckingAccountRequest createCheckingAccountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException;

    GeneralResponse blockAccount(long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException;

    GeneralResponse getCustomersUnblockedCheckingAccounts(long customerID) throws CustomerNotFoundException;

    GeneralResponse getCheckingAccountActivities(long id, long accountID, ActivityFilter filter) throws CustomerNotFoundException, CheckingAccountNotFoundException, ActivitiesNotFoundException;

    GeneralResponse getCheckingAccountById(Long customerID, long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException;
}
