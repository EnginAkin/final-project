package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.core.exception.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException() {
        super("Role not Found");
    }
}
