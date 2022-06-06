package org.norma.finalproject.common.security.token.service;

import org.norma.finalproject.common.security.token.core.exception.TokenNotFoundException;
import org.norma.finalproject.common.security.token.entity.JWTToken;

public interface TokenService {

    JWTToken getToken(String token) throws TokenNotFoundException;

    JWTToken save(JWTToken JWTToken);

    void delete(String token) throws TokenNotFoundException;

}
