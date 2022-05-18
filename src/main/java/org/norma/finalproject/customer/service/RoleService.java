package org.norma.finalproject.customer.service;

import org.norma.finalproject.customer.core.exception.RoleNotFoundException;
import org.norma.finalproject.customer.entity.Role;

public interface RoleService {

    Role getRoleByName(String role);

}
