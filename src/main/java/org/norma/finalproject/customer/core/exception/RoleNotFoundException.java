package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.exception.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException() {
        super("Role not Found");
    }
}
