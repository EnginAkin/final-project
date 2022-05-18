package org.norma.finalproject.customer.core.exception;

import org.norma.finalproject.common.exception.BusinessException;

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException() {
        super("Role not Found");
    }
}
