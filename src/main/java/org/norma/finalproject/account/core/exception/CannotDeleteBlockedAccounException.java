package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class CannotDeleteBlockedAccounException extends BusinessException {
    public CannotDeleteBlockedAccounException() {
        super("Cannot delete blocked account. You cannot access anything this blocked account");
    }
}
