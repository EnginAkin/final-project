package org.norma.finalproject.customer.core.exception.customer;

import org.norma.finalproject.common.exception.BusinessException;

public class NotAcceptableAgeException extends BusinessException {
    public NotAcceptableAgeException() {
        super("You must be over 18 years old.");
    }
}
