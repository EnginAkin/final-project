package org.norma.finalproject.customer.service;

import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;


public interface FacadeCustomerService {

    GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, IdentityNotValidException, CustomerAlreadyRegisterException, CustomerNotFoundException, AccountNameAlreadyHaveException;

    GeneralResponse update(Long CustomerId, UpdateCustomerRequest updateCustomerRequest) throws CustomerNotFoundException, UpdateCustomerSamePasswordException;

    GeneralResponse delete(Long customerId) throws CustomerNotFoundException, CustomerDeleteException;

    GeneralResponse getall();

}
