package org.norma.finalproject.customer.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.RoleRepository;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl underTest;

    @Test
    public void given_when_then(){
        // given
        String roleName="USER_ROLE";
        Role userRole=new Role(roleName);
        roleRepository.save(userRole);
        // when
        Role testRoleByName = underTest.getRoleByName(roleName);
        // then
        Assertions.assertThat(testRoleByName).isNotNull();
        Assertions.assertThat(testRoleByName.getName()).isEqualTo(roleName);

    }

}