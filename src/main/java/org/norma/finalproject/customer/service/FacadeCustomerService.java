package org.norma.finalproject.customer.service;

import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;
import org.norma.finalproject.customer.entity.Customer;


public interface FacadeCustomerService {

    GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, IdentityNotValidException, CustomerAlreadyRegisterException, CustomerNotFoundException, AccountNameAlreadyHaveException;

    GeneralResponse update(Long CustomerId, UpdateCustomerRequest updateCustomerRequest) throws CustomerNotFoundException, UpdateCustomerSamePasswordException;

    GeneralResponse delete(Long customerId) throws CustomerNotFoundException, CustomerDeleteException;
}
