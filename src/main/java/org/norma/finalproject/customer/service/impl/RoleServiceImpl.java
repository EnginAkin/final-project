package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.RoleRepository;
import org.norma.finalproject.customer.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;


    @Override
    public Role getRoleByName(String role) {
        return repository.findByName(role).get();

    }

    @PostConstruct
    public void addRoleDatabase() {
        Optional<Role> optionalRole = repository.findByName(CustomerConstant.ROLE_USER);
        if (optionalRole.isEmpty()) {
            Role roleUser = new Role(CustomerConstant.ROLE_USER);
            Role roleAdmin = new Role(CustomerConstant.ROLE_ADMIN);
            repository.saveAll(List.of(roleAdmin, roleUser));
        }
    }

}

