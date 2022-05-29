package org.norma.finalproject.customer.service;

import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.LoginFailedException;

public interface AuthService {

    GeneralResponse login(String identity, String password) throws LoginFailedException;

}
