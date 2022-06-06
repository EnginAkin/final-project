package org.norma.finalproject.common.security.token.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class TokenNotFoundException extends BusinessException {
    public TokenNotFoundException() {
        super("Token not found");
    }
}
