package org.norma.finalproject.card.service;

import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface DebitFacadeService {

   GeneralDataResponse create(long customerID,CreateDebitCardRequest createDebitCardRequest) throws DebitCardOperationException, DebitCardNotFoundException, CheckingAccountNotFoundException, CustomerNotFoundException;

   GeneralDataResponse getByID(Long id, long debitID) throws CustomerNotFoundException, DebitCardNotFoundException;

   GeneralDataResponse getAllCustomersDebitCards(Long customerID) throws CustomerNotFoundException, DebitCardOperationException;

   GeneralResponse update(Long CustomerID, long debitCardID, UpdateDebitCardRequest updateDebitCardRequest) throws CustomerNotFoundException, DebitCardOperationException;

}
