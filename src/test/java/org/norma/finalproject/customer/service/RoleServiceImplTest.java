package org.norma.finalproject.customer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.RoleRepository;
import org.norma.finalproject.customer.service.impl.RoleServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl underTest;

    @Test
    public void givenUserRole_whenGetRoleByName_thenReturnsRoleObject() {
        // given
        String roleName = "USER_ROLE";
        Role userRole = new Role(roleName);
        roleRepository.save(userRole);
        BDDMockito.given(roleRepository.findByName(roleName)).willReturn(Optional.of(userRole));
        // when
        Role testRoleByName = underTest.getRoleByName(roleName);
        // then
        Assertions.assertThat(testRoleByName).isNotNull();
        Assertions.assertThat(testRoleByName.getName()).isEqualTo(roleName);

    }

}