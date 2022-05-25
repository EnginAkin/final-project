package org.norma.finalproject.account.core.exception;

import org.norma.finalproject.common.exception.NotFoundException;

public class SavingAccountNotFound extends NotFoundException {
    public SavingAccountNotFound() {
        super("saving account not found.");
    }
}
