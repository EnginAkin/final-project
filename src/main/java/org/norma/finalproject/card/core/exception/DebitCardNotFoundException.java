package org.norma.finalproject.card.core.exception;

import org.norma.finalproject.common.core.exception.NotFoundException;

public class DebitCardNotFoundException extends NotFoundException {
    public DebitCardNotFoundException() {
        super("Debit card not found.");
    }
}
