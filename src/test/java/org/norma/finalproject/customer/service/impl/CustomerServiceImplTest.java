package org.norma.finalproject.customer.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.customer.core.exception.CustomerAlreadyRegisterException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl underTest;

    @Test
    void givenCustomer_whenSave_thenReturnSavedCustomer() throws CustomerAlreadyRegisterException {
        // given
        Customer customer = Customer.builder().name("engin").password("123").customerNo("213").birthDay(new Date()).identityNumber("123").build();
        BDDMockito.given(customerRepository.save(customer)).willReturn(customer);
        // when
        Customer savedCustomer = underTest.save(customer);
        // then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer).isEqualTo(customer);

    }


}