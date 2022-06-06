package org.norma.finalproject.account.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.mapper.AccountActivityMapper;
import org.norma.finalproject.account.core.mapper.CheckingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.account.service.FacadeSavingAccountService;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class FacadaCheckingAccountServiceTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private CheckingAccountService checkingAccountService;
    @Mock
    private FacadeSavingAccountService facadeSavingAccountService;
    @Mock
    private UniqueNoCreator uniqueNoCreator;
    @Mock
    private CheckingAccountMapper checkingAccountMapper;
    @Mock
    private AccountActivityMapper accountActivityMapper;

    @Spy
    @InjectMocks
    private FacadeCheckingAccountServiceImpl underTest;

    @Test
    public void givenCustomerIdAndCreateCheckingAccountRequest_whenCreate_thenReturnGeneralDataResponse() throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        // given
        CreateCheckingAccountRequest createCheckingAccountRequest = createCheckingAccountRequest();

        Long customerID = 1L;
        Customer customer = createCustomer();
        customer.setId(customerID);
        CheckingAccount checkingAccount = createCheckingAccount();

        customer.addCheckingAccount(checkingAccount);

        CreateDepositAccountResponse createDepositAccountResponse = createDepositAccountResponse(checkingAccount);


        String accountName = createCheckingAccountRequest.getBranchName() + "-" + createCheckingAccountRequest.getBranchCode() + "/" + createCheckingAccountRequest.getBankCode();
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(underTest.findCheckingAccountByAccountName(customer.getCheckingAccounts(), accountName, createCheckingAccountRequest.getCurrencyType())).willReturn(Optional.empty());
        BDDMockito.given(checkingAccountMapper.toEntity(createCheckingAccountRequest)).willReturn(checkingAccount);
        BDDMockito.given(uniqueNoCreator.creatAccountNo()).willReturn("1111111111111111");
        BDDMockito.given(uniqueNoCreator.createIbanNo("1111111111111111", createCheckingAccountRequest.getBankCode())).willReturn("TR00000000");
        BDDMockito.given(checkingAccountService.save(checkingAccount)).willReturn(checkingAccount);
        BDDMockito.given(checkingAccountMapper.toCreateCheckingAccountDto(checkingAccount)).willReturn(createDepositAccountResponse);

        // when
        GeneralDataResponse dataResponse = underTest.create(customerID, createCheckingAccountRequest);

        // then

        Assertions.assertThat(dataResponse.getIsSuccessful()).isTrue();
        Assertions.assertThat(dataResponse.getData()).isEqualTo(createDepositAccountResponse);


    }

    @Test
    public void givenInvalidCustomerId_whenCreate_thenThrowsCustomerNotFoundException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        // given
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.empty());
        // then
        assertThrows(CustomerNotFoundException.class, () -> underTest.create(customerID, createCheckingAccountRequest()));
    }

    @Test
    public void givenAlreadyHaveAccountName_whenCreate_thenThrowsAccountNameAlreadyHaveException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        // given
        Long customerID = 1L;
        Customer customer = createCustomer();
        CheckingAccount checkingAccount = createCheckingAccount();
        customer.addCheckingAccount(checkingAccount);
        CreateCheckingAccountRequest createCheckingAccountRequest = createCheckingAccountRequest();
        String accountName = createCheckingAccountRequest.getBranchName() + "-" + createCheckingAccountRequest.getBranchCode() + "/" + createCheckingAccountRequest.getBankCode();

        // when
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(underTest.findCheckingAccountByAccountName(customer.getCheckingAccounts(), accountName, createCheckingAccountRequest.getCurrencyType())).willReturn(Optional.of(checkingAccount));

        // then
        AccountNameAlreadyHaveException exception = assertThrows(AccountNameAlreadyHaveException.class, () -> {
            underTest.create(customerID, createCheckingAccountRequest);
        });
        // then
        Assertions.assertThat(exception.getMessage()).isEqualTo("You have a already bank account in this bank same currency and same branch .");
    }

    @Test
    public void givenCustomerId_whenBlockCheckingAccount_thenReturnGeneralSuccessfull() throws CheckingAccountNotFoundException {
        // given
        CheckingAccount checkingAccount = createCheckingAccount();
        checkingAccount.setId(1L);
        BDDMockito.given(checkingAccountService.findById(checkingAccount.getId())).willReturn(Optional.of(checkingAccount));
        BDDMockito.given(checkingAccountService.save(checkingAccount)).willReturn(checkingAccount);
        // when
        GeneralResponse blockAccountResponse = underTest.blockAccount(checkingAccount.getId());
        // then
        Assertions.assertThat(blockAccountResponse.getIsSuccessful()).isTrue();
    }

    @Test
    public void givenInvalidAccountId_whenBlockAccount_thenThrowsCheckingAccountNotFoundException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {

        // given
        Long accountId = 1L;
        BDDMockito.given(checkingAccountService.findById(accountId)).willReturn(Optional.empty());
        // then
        assertThrows(CheckingAccountNotFoundException.class, () -> underTest.blockAccount(accountId));
        Mockito.verify(checkingAccountService, times(0)).save(createCheckingAccount());

    }


    private CreateDepositAccountResponse createDepositAccountResponse(CheckingAccount checkingAccount) {
        CreateDepositAccountResponse createDepositAccountResponse = new CreateDepositAccountResponse();
        createDepositAccountResponse.setAccountNo(checkingAccount.getAccountNo());
        createDepositAccountResponse.setAccountName(checkingAccount.getAccountName());
        createDepositAccountResponse.setIbanNo(checkingAccount.getIbanNo());
        return createDepositAccountResponse;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("Engin");
        customer.setSurname("akin");
        customer.setTelephone("11111");
        customer.setPassword("123545");
        customer.setEmail("email");
        customer.setBirthDay(new Date());
        customer.setIdentityNumber("111111");
        customer.setIncome(BigDecimal.ONE);
        return customer;
    }

    private CheckingAccount createCheckingAccount() {
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setBranchCode("00");
        checkingAccount.setCurrencyType(CurrencyType.TRY);
        checkingAccount.setBlocked(false);
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("bulunamadı.");
        checkingAccount.setBranchName("FATIH");
        return checkingAccount;
    }

    private CreateCheckingAccountRequest createCheckingAccountRequest() {
        CreateCheckingAccountRequest checkingAccountRequest = new CreateCheckingAccountRequest();
        checkingAccountRequest.setBankCode("000000");
        checkingAccountRequest.setBranchName("FATIH");
        checkingAccountRequest.setBranchCode("00");
        checkingAccountRequest.setCurrencyType(CurrencyType.TRY);
        return checkingAccountRequest;
    }


}
