package org.norma.finalproject.card.core.exception;

import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.exception.NotFoundException;

public class DebitCardNotFoundException extends NotFoundException {
    public DebitCardNotFoundException(String message) {
        super(message);
    }
}
