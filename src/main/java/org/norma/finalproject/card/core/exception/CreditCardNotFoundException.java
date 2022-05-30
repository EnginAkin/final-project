package org.norma.finalproject.card.core.exception;

import org.norma.finalproject.common.core.exception.NotFoundException;

public class CreditCardNotFoundException extends NotFoundException {
    public CreditCardNotFoundException() {
        super("Credit card not found.");
    }
}
