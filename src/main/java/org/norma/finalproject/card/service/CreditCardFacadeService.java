package org.norma.finalproject.card.service;

import org.norma.finalproject.card.core.model.request.CreateCreditCardRequest;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface CreditCardFacadeService {

    GeneralResponse  create(long userID, CreateCreditCardRequest createCreditCardRequest) throws CustomerNotFoundException;
}