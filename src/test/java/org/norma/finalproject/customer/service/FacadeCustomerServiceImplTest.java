package org.norma.finalproject.customer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.mapper.CustomerMapper;
import org.norma.finalproject.customer.core.model.AddressDto;
import org.norma.finalproject.customer.core.model.CustomerInfoDto;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Address;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.enums.AddressType;
import org.norma.finalproject.customer.service.impl.FacadeCustomerServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class FacadeCustomerServiceImplTest {
    @Mock
    private CustomerService customerService;
    @Mock
    private IdentityVerifier identityVerifier;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private FacadeCheckinAccountService facadeCheckinAccountService;
    @Spy
    @InjectMocks
    private FacadeCustomerServiceImpl underTest;


    @Test
    public void givenCreateCustomerRequest_whenSignup_thenReturnSuccessfullResponse() throws CustomerNotFoundException, AccountNameAlreadyHaveException, NotAcceptableAgeException, CustomerAlreadyRegisterException, IdentityNotValidException {
        // given
        CustomerInfoDto customerInfoDto = getCustomerInfoDto();
        AddressDto addressDto = getAddressDto();
        CreateCheckingAccountRequest checkingAccountRequest = getCreateCheckingAccountRequest();
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setCustomerInfo(customerInfoDto);
        createCustomerRequest.setAddress(addressDto);
        createCustomerRequest.setCheckingAccount(checkingAccountRequest);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail(customerInfoDto.getEmail());
        customer.setPassword(customerInfoDto.getPassword());
        customer.setName(customerInfoDto.getName());
        customer.setIdentityNumber(customerInfoDto.getIdentityNumber());
        customer.setBirthDay(customerInfoDto.getBirthDay());
        Address address = new Address();
        address.setState(addressDto.getState());
        address.setAddressType(addressDto.getAddressType());
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setCustomer(customer);
        customer.setAddresses(Set.of(address));
        BDDMockito.given(customerMapper.customerInfoDtoToCustomer(createCustomerRequest)).willReturn(customer);
        BDDMockito.given(identityVerifier.verify(customer.getIdentityNumber())).willReturn(true);
        BDDMockito.given(customerService.save(customer)).willReturn(customer);
        BDDMockito.given(facadeCheckinAccountService.create(customer.getId(), checkingAccountRequest)).willReturn(new GeneralSuccessfullResponse("succesfful"));
        // when
        GeneralResponse response = underTest.signup(createCustomerRequest);
        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo(CustomerConstant.SIGNUP_SUCESSFULL);
        Assertions.assertThat(response.getIsSuccessful()).isTrue();

    }

    @Test
    public void givenCreateCustomerRequestWithInvalidIdentity_whenSignup_thenReturnThrowsIdentityNotValidException() throws CustomerNotFoundException, AccountNameAlreadyHaveException, NotAcceptableAgeException, CustomerAlreadyRegisterException, IdentityNotValidException {
        // given
        CustomerInfoDto customerInfoDto = getCustomerInfoDto();
        AddressDto addressDto = getAddressDto();
        CreateCheckingAccountRequest checkingAccountRequest = getCreateCheckingAccountRequest();
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setCustomerInfo(customerInfoDto);
        createCustomerRequest.setAddress(addressDto);
        createCustomerRequest.setCheckingAccount(checkingAccountRequest);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail(customerInfoDto.getEmail());
        customer.setPassword(customerInfoDto.getPassword());
        customer.setName(customerInfoDto.getName());
        customer.setIdentityNumber("invalid");
        customer.setBirthDay(customerInfoDto.getBirthDay());
        Address address = new Address();
        address.setState(addressDto.getState());
        address.setAddressType(addressDto.getAddressType());
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setCustomer(customer);
        customer.setAddresses(Set.of(address));
        BDDMockito.given(customerMapper.customerInfoDtoToCustomer(createCustomerRequest)).willReturn(customer);
        BDDMockito.given(identityVerifier.verify(customer.getIdentityNumber())).willReturn(false);
        // when
        IdentityNotValidException exception = assertThrows(IdentityNotValidException.class, () -> {
            underTest.signup(createCustomerRequest);
        });
        // then
        String expectedExceptionMessage = "Identity not valid.";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void givenInvalidCustomerIdAndUpdateCustomerRequest_whenUpdate_ThenThrowsCustomerNotFound() throws UpdateCustomerSamePasswordException, CustomerNotFoundException {
        // given
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setPassword("new password");
        request.setTelephone("new telephone");
        BDDMockito.given(customerService.findByCustomerById(customerId)).willReturn(Optional.empty());
        // when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            underTest.update(customerId, request);
        });
        //then
        Assertions.assertThat(exception).isNotNull();
    }

    @Test
    public void givenCustomerIdAndInvalidUpdateCustomerRequest_whenUpdate_ThenThrowsUpdateCustomerSamePasswordException() throws UpdateCustomerSamePasswordException, CustomerNotFoundException {
        // given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setPassword("old password");
        customer.setTelephone("old telephone");
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setPassword("new password");
        request.setTelephone("new telephone");
        BDDMockito.given(customerService.findByCustomerById(customerId)).willReturn(Optional.of(customer));
        BDDMockito.given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(true);
        // when
        UpdateCustomerSamePasswordException exception = assertThrows(UpdateCustomerSamePasswordException.class, () -> {
            underTest.update(customerId, request);
        });
        //then
        Assertions.assertThat(exception).isNotNull();
    }

    @Test
    public void givenCustomerIdAndUpdateCustomerRequest_whenUpdate_ThenReturnGeneralSuccessfullyResponse() throws UpdateCustomerSamePasswordException, CustomerNotFoundException {
        // given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setPassword("old password");
        customer.setTelephone("old telephone");
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setPassword("new password");
        request.setTelephone("new telephone");
        BDDMockito.given(customerService.findByCustomerById(customerId)).willReturn(Optional.of(customer));
        BDDMockito.given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(false);
        doNothing().when(customerService).update(customer);
        // when
        GeneralResponse response = underTest.update(customerId, request);
        //then
        Assertions.assertThat(response.getIsSuccessful()).isTrue();
        Assertions.assertThat(response.getMessage()).isEqualTo(CustomerConstant.UPDATE_SUCESSFULL);

    }

    @Test
    public void givenCustomerId_whenDelete_thenReturnGeneralSuccessfullyResponse() throws CustomerDeleteException, CustomerNotFoundException {
        // given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setPassword("old password");
        customer.setTelephone("old telephone");
        BDDMockito.given(customerService.findByCustomerById(customerId)).willReturn(Optional.of(customer));
        BDDMockito.given(underTest.checkHasMoneyInCustomerAccounts(customer)).willReturn(false);
        doNothing().when(customerService).delete(customer);
        // when
        GeneralResponse response = underTest.delete(customerId);
        // then
        Assertions.assertThat(response.getIsSuccessful()).isTrue();
        Assertions.assertThat(response.getMessage()).isEqualTo(CustomerConstant.CUSTOMER_DELETED_SUCCESSFULL);

    }

    @Test
    public void givenInvalidCustomerId_whenDelete_thenReturnThrowsCustomerNotFoundException() throws CustomerDeleteException, CustomerNotFoundException {
        // given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setPassword("old password");
        customer.setTelephone("old telephone");
        BDDMockito.given(customerService.findByCustomerById(customerId)).willReturn(Optional.empty());
        // when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            underTest.delete(customerId);
        });
        //then
        Assertions.assertThat(exception).isNotNull();

    }

    @Test
    public void givenHasMoneyInBalanceCustomer_whenDelete_thenReturnThrowsCustomerDeleteException() throws CustomerDeleteException, CustomerNotFoundException {
        // given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setPassword("old password");
        customer.setTelephone("old telephone");
        BDDMockito.given(customerService.findByCustomerById(customerId)).willReturn(Optional.of(customer));
        BDDMockito.given(underTest.checkHasMoneyInCustomerAccounts(customer)).willReturn(true);
        // when
        CustomerDeleteException exception = assertThrows(CustomerDeleteException.class, () -> {
            underTest.delete(customerId);
        });
        //then
        Assertions.assertThat(exception).isNotNull();

    }

    private CreateCheckingAccountRequest getCreateCheckingAccountRequest() {
        CreateCheckingAccountRequest request = new CreateCheckingAccountRequest();
        request.setBankCode("000000");
        request.setBankCode("00");
        request.setBranchName("FATIH");
        request.setCurrencyType(CurrencyType.TRY);
        return request;
    }

    private AddressDto getAddressDto() {
        AddressDto addressDto = new AddressDto();
        addressDto.setAddressType(AddressType.HOME);
        addressDto.setCity("Mersin");
        addressDto.setCountry("Turkey");
        addressDto.setState("Akdeniz");
        addressDto.setDistrict("Çilek");
        addressDto.setStreetNumber("6390");
        return addressDto;
    }

    private CustomerInfoDto getCustomerInfoDto() {
        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        customerInfoDto.setPassword("password");
        customerInfoDto.setEmail("email");
        customerInfoDto.setTelephone("4444");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, 03, 03);
        customerInfoDto.setBirthDay(calendar.getTime());
        customerInfoDto.setIncome(BigDecimal.ONE);
        customerInfoDto.setIdentityNumber("111111111");
        customerInfoDto.setName("ENGIN");
        customerInfoDto.setSurname("akin");
        return customerInfoDto;
    }
}