package org.norma.finalproject.customer.service;

import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerAlreadyRegisterException;
import org.norma.finalproject.customer.core.exception.IdentityNotValidException;
import org.norma.finalproject.customer.core.exception.NotAcceptableAgeException;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;


public interface FacadeCustomerService {

    GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws IdentityNotValidException, NotAcceptableAgeException, NotAcceptableAgeException, IdentityNotValidException, CustomerAlreadyRegisterException;

}
