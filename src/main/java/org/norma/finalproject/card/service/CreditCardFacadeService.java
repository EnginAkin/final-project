package org.norma.finalproject.card.service;

import org.norma.finalproject.card.core.exception.CreditCardNotFoundException;
import org.norma.finalproject.card.core.exception.CreditCardOperationException;
import org.norma.finalproject.card.core.model.request.CreateCreditCardRequest;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface CreditCardFacadeService {

    GeneralResponse create(long userID, CreateCreditCardRequest createCreditCardRequest) throws CustomerNotFoundException, CreditCardOperationException;

    GeneralResponse getCurrentTermTransactions(Long userID, long creditCardID) throws CustomerNotFoundException, CreditCardNotFoundException, CreditCardOperationException;

    GeneralDataResponse getCustomerCreditCards(Long userID) throws CustomerNotFoundException;

    GeneralResponse getCreditCardDebt(Long userID, long creditCardID) throws CustomerNotFoundException, CreditCardOperationException, CreditCardNotFoundException;

    GeneralResponse deleteCreditCard(Long userID, long creditCardId) throws CustomerNotFoundException, CreditCardNotFoundException, CreditCardOperationException;
}
