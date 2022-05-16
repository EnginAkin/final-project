package org.norma.finalproject.customer.service;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.customer.IdentityNotValidException;
import org.norma.finalproject.customer.core.exception.customer.NotAcceptableAgeException;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.springframework.stereotype.Service;


public interface FacadeCustomerService {

    GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws IdentityNotValidException, NotAcceptableAgeException;

}
