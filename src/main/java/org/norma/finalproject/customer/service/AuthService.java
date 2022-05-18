package org.norma.finalproject.customer.service;

import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.LoginFailedException;

public interface AuthService {

    GeneralResponse login(String identity, String password) throws LoginFailedException;

}
