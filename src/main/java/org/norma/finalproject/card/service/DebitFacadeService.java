package org.norma.finalproject.card.service;

import org.norma.finalproject.card.core.exception.DebitOperationException;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface DebitFacadeService {

   GeneralDataResponse create(long customerID, CreateDebitCardRequest createDebitCardRequest) throws CustomerNotFoundException, DebitOperationException;

   GeneralDataResponse getByID(Long id, long debitID) throws CustomerNotFoundException, DebitOperationException;

   GeneralDataResponse getAllCustomersDebitCards(Long customerID) throws CustomerNotFoundException, DebitOperationException;

   GeneralResponse update(Long CustomerID, long debitCardID, UpdateDebitCardRequest updateDebitCardRequest) throws CustomerNotFoundException, DebitOperationException;
}
