package org.norma.finalproject.customer.service;

import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;


public interface FacadeCustomerService {

    GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, IdentityNotValidException, CustomerAlreadyRegisterException;

    GeneralResponse update(long id, UpdateCustomerRequest updateCustomerRequest) throws CustomerNotFoundException, UpdateCustomerSamePasswordException;

    GeneralResponse delete(long id) throws CustomerNotFoundException, CustomerDeleteException;
}
