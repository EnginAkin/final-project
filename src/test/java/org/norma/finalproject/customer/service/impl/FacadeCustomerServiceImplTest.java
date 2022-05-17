package org.norma.finalproject.customer.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerAlreadyRegisterException;
import org.norma.finalproject.customer.core.exception.IdentityNotValidException;
import org.norma.finalproject.customer.core.exception.NotAcceptableAgeException;
import org.norma.finalproject.customer.core.mapper.CustomerMapper;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.core.utilities.Utils;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.customer.service.IdentityVerifier;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class FacadeCustomerServiceImplTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private IdentityVerifier identityVerifier;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private FacadeCustomerServiceImpl underTest;

    @Test
    void givenValidCreateCustomerRequest_whenSignup_thenSignedIn() throws NotAcceptableAgeException, IdentityNotValidException, CustomerAlreadyRegisterException {
        // given
        MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class);
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(1998, 3, 3);
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("engin");
        createCustomerRequest.setSurname("engin");
        createCustomerRequest.setIdentityNumber("11111111111");
        createCustomerRequest.setPassword("1234");
        createCustomerRequest.setTelephone("1234");

        Customer customer = Customer.builder().birthDay(createCustomerRequest.getBirthDay())
                .name(createCustomerRequest.getName())
                .surname(createCustomerRequest.getSurname())
                .identityNumber(createCustomerRequest.getIdentityNumber())
                .password(createCustomerRequest.getPassword()).build();

        BDDMockito.given(identityVerifier.verify(customer.getIdentityNumber())).willReturn(true);

        utilities.when(() -> Utils.isOver18YearsOld(customer.getBirthDay()))
                .thenReturn(true);


        BDDMockito.given(customerMapper.customerDtoToCustomer(createCustomerRequest)).willReturn(customer);
        // when
        GeneralResponse response = underTest.signup(createCustomerRequest);
        // then
        assertThat(response.getIsSuccessful()).isTrue();
        assertThat(response.getMessage()).isEqualTo(CustomerConstant.SIGNUP_SUCESSFULL);
        Mockito.verify(customerService, Mockito.atMost(1)).save(customer);

        utilities.close();

    }



    @Test
    void givenUnder18Age_whenSignup_thenThrowsNotAcceptableAgeException() throws CustomerAlreadyRegisterException {
        // given
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(1998, 3, 3);
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("engin");
        createCustomerRequest.setSurname("engin");
        createCustomerRequest.setIdentityNumber("invalid identity");
        createCustomerRequest.setPassword("1234");
        createCustomerRequest.setTelephone("1234");

        Customer customer = Customer.builder().birthDay(createCustomerRequest.getBirthDay())
                .name(createCustomerRequest.getName())
                .surname(createCustomerRequest.getSurname())
                .identityNumber(createCustomerRequest.getIdentityNumber())
                .password(createCustomerRequest.getPassword()).build();
        BDDMockito.given(customerMapper.customerDtoToCustomer(createCustomerRequest)).willReturn(customer);
        BDDMockito.given(identityVerifier.verify(customer.getIdentityNumber())).willReturn(false);


        // when
        Assertions.assertThrows(IdentityNotValidException.class, () -> {
            underTest.signup(createCustomerRequest);
        });

        Mockito.verify(customerService, Mockito.atMost(0)).save(customer);


    }

    @Test
    void givenInvalidIdentity_whenSignup_thenThrowsIdentityNotValidException() throws CustomerAlreadyRegisterException {
        // given
        MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class);
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(1998, 3, 3);
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("engin");
        createCustomerRequest.setSurname("engin");
        createCustomerRequest.setIdentityNumber("invalid identity");
        createCustomerRequest.setPassword("1234");
        createCustomerRequest.setTelephone("1234");

        Customer customer = Customer.builder().birthDay(createCustomerRequest.getBirthDay())
                .name(createCustomerRequest.getName())
                .surname(createCustomerRequest.getSurname())
                .identityNumber(createCustomerRequest.getIdentityNumber())
                .password(createCustomerRequest.getPassword()).build();
        BDDMockito.given(customerMapper.customerDtoToCustomer(createCustomerRequest)).willReturn(customer);
        BDDMockito.given(identityVerifier.verify(customer.getIdentityNumber())).willReturn(true);
        utilities.when(() -> Utils.isOver18YearsOld(customer.getBirthDay()))
                .thenReturn(false);

        // when
        Assertions.assertThrows(NotAcceptableAgeException.class, () -> {
            underTest.signup(createCustomerRequest);
        });

        Mockito.verify(customerService, Mockito.atMost(0)).save(customer);

        utilities.close();


    }
}