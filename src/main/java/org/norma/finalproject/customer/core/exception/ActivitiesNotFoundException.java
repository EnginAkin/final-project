package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.core.exception.NotFoundException;

public class ActivitiesNotFoundException extends NotFoundException {
    public ActivitiesNotFoundException() {
        super("Activities not found.");
    }
}
