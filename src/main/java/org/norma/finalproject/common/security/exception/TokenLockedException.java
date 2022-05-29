package org.norma.finalproject.common.security.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class TokenLockedException extends BusinessException {
    public TokenLockedException() {
        super("Token Locked.Please Login again.");
    }
}
