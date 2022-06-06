package org.norma.finalproject.card.service;

import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface DebitFacadeService {

    GeneralDataResponse create(long customerID, CreateDebitCardRequest createDebitCardRequest) throws DebitCardOperationException, DebitCardNotFoundException, CheckingAccountNotFoundException, CustomerNotFoundException;

    GeneralDataResponse getDebitCardByID(Long id, long debitID) throws CustomerNotFoundException, DebitCardNotFoundException;

    GeneralDataResponse getAllCustomerDebitCards(Long customerID) throws CustomerNotFoundException, DebitCardNotFoundException;

    GeneralResponse update(Long CustomerID, long debitCardID, UpdateDebitCardRequest updateDebitCardRequest) throws CustomerNotFoundException, DebitCardOperationException;

    GeneralResponse deleteDebitCardById(Long customerID, long debitCardID) throws CustomerNotFoundException, DebitCardOperationException;

    GeneralResponse getDebitCardActivities(Long customerID, long debitCardID, ActivityFilter filter) throws CustomerNotFoundException, DebitCardNotFoundException;
}
