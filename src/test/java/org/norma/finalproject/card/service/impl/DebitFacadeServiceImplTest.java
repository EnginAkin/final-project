package org.norma.finalproject.card.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.mapper.AccountActivityMapper;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.mapper.DebitCardMapper;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.entity.enums.CardStatus;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebitFacadeServiceImplTest {
    @Mock
    private CustomerService customerService;
    @Mock
    private CheckingAccountService checkingAccountService;
    @Mock
    private UniqueNoCreator uniqueNoCreator;
    @Mock
    private DebitCardService debitCardService;
    @Mock
    private DebitCardMapper debitCardMapper;
    @Mock
    private AccountActivityMapper accountActivityMapper;

    @InjectMocks
    private DebitFacadeServiceImpl underTest;


    @Test
    public void givenCreateDebitCardRequest_whenCreate_thenReturnSuccessfullyResponse() throws DebitCardOperationException, CustomerNotFoundException, CheckingAccountNotFoundException {
        // given
        Customer customer = createCustomer();
        DebitCard debitCard = createDebitCard();
        CheckingAccount checkingAccount = createCheckingAccount();
        checkingAccount.setCustomer(customer);
        debitCard.setCheckingAccount(checkingAccount);
        DebitCardResponse response = new DebitCardResponse();
        response.setPassword(debitCard.getPassword());
        CreateDebitCardRequest debitCardRequest = new CreateDebitCardRequest();
        debitCardRequest.setPassword("11111");
        debitCardRequest.setParentCheckingAccountId(debitCard.getId());
        given(customerService.findByCustomerById(customer.getId())).willReturn(Optional.of(customer));
        given(checkingAccountService.findById(debitCardRequest.getParentCheckingAccountId())).willReturn(Optional.of(checkingAccount));
        given(debitCardService.existsDebitCardByCheckingAccountId(checkingAccount.getId())).willReturn(false);
        given(uniqueNoCreator.creatAccountNo()).willReturn("1111111");
        when(debitCardService.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(debitCardMapper.toDto(any())).thenReturn(response);

        // when
        GeneralDataResponse generalDataResponse = underTest.create(customer.getId(), debitCardRequest);
        // then
        Assertions.assertThat(generalDataResponse.getIsSuccessful()).isTrue();
    }

    @Test
    public void givenInvalidCustomerId_whenCreate_thenThrowsCustomerNotFoundException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        // given
        Customer customer = createCustomer();
        CreateDebitCardRequest debitCardRequest = new CreateDebitCardRequest();
        debitCardRequest.setPassword("11111");
        debitCardRequest.setParentCheckingAccountId(1L);
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.empty());
        // then
        assertThrows(CustomerNotFoundException.class, () -> underTest.create(customerID, debitCardRequest));
    }

    @Test
    public void givenInvalidCustomerIdAndCheckingAccountId_whenCreate_thenThrowsCheckinAccountNotFoundException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        // given
        Customer customer = createCustomer();
        CreateDebitCardRequest debitCardRequest = new CreateDebitCardRequest();
        debitCardRequest.setPassword("11111");
        debitCardRequest.setParentCheckingAccountId(1L);
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(checkingAccountService.findById(1L)).willReturn(Optional.empty());
        // then
        assertThrows(CheckingAccountNotFoundException.class, () -> underTest.create(customerID, debitCardRequest));
    }

    @Test
    public void givenExistsDebitCardByCheckingAccount_whenCreate_thenThrowsDebitCardOperationException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        // given
        CreateDebitCardRequest debitCardRequest = new CreateDebitCardRequest();
        CheckingAccount checkingAccount = createCheckingAccount();
        Customer customer = createCustomer();
        checkingAccount.setCustomer(customer);
        debitCardRequest.setPassword("11111");
        debitCardRequest.setParentCheckingAccountId(1L);
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(checkingAccountService.findById(debitCardRequest.getParentCheckingAccountId())).willReturn(Optional.of(checkingAccount));
        BDDMockito.given(debitCardService.existsDebitCardByCheckingAccountId(debitCardRequest.getParentCheckingAccountId())).willReturn(true);
        // then
        assertThrows(DebitCardOperationException.class, () -> underTest.create(customerID, debitCardRequest));
    }

    @Test
    public void givenInvalidParentAccountId_whenCreate_thenThrowsCheckingAccountNotFoundException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        // given
        CreateDebitCardRequest debitCardRequest = new CreateDebitCardRequest();
        Customer customer = createCustomer();
        debitCardRequest.setPassword("11111");
        debitCardRequest.setParentCheckingAccountId(1L);
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        // then
        assertThrows(CheckingAccountNotFoundException.class, () -> underTest.create(customerID, debitCardRequest));
    }

    @Test
    public void givenDebitCardId_whenGetDebitCardActivities_thenReturnSuccessfullyResponse() throws DebitCardNotFoundException, CustomerNotFoundException {
        // given
        DebitCard debitCard = createDebitCard();
        CheckingAccount checkingAccount = createCheckingAccount();
        checkingAccount.setActivities(Collections.emptyList());
        debitCard.setCheckingAccount(checkingAccount);
        Customer customer = createCustomer();
        BDDMockito.given(customerService.findByCustomerById(customer.getId())).willReturn(Optional.of(customer));
        BDDMockito.given(debitCardService.findDebitCardWithCustomerIDAndCardID(customer.getId(), debitCard.getId())).willReturn(Optional.of(debitCard));
        // when
        GeneralResponse response = underTest.getDebitCardActivities(customer.getId(), debitCard.getId(), null);
        // then
        Assertions.assertThat(response.getIsSuccessful()).isTrue();
    }

    @Test
    public void givenDebitCardId_whenGetDebitCardByID_thenReturnGeneralDataResponse() throws DebitCardNotFoundException, CustomerNotFoundException {
        // given
        DebitCard debitCard = createDebitCard();
        Customer customer = createCustomer();
        DebitCardResponse response = new DebitCardResponse();
        BDDMockito.given(customerService.findByCustomerById(customer.getId())).willReturn(Optional.of(customer));
        BDDMockito.given(debitCardService.findDebitCardWithCustomerIDAndCardID(customer.getId(), debitCard.getId())).willReturn(Optional.of(debitCard));
        BDDMockito.given(debitCardMapper.toDto(debitCard)).willReturn(response);
        // when
        GeneralDataResponse generalDataResponse = underTest.getDebitCardByID(customer.getId(), debitCard.getId());
        // then
        Assertions.assertThat(generalDataResponse).isNotNull();
        Assertions.assertThat(generalDataResponse.getIsSuccessful()).isTrue();
    }


    @Test
    public void givenCustomerID_whenGetAllCustomerDebitCards_thenReturnSuccessfully() throws DebitCardNotFoundException, CustomerNotFoundException {
        // given
        DebitCard debitCard1 = createDebitCard();
        DebitCard debitCard2 = createDebitCard();
        Customer customer = createCustomer();
        List<DebitCard> debitCardList = List.of(debitCard2, debitCard1);
        given(customerService.findByCustomerById(customer.getId())).willReturn(Optional.of(customer));
        given(debitCardService.getAllCustomersDebitCards(customer.getId())).willReturn(debitCardList);
        // when
        GeneralDataResponse response = underTest.getAllCustomerDebitCards(customer.getId());
        // then
        Assertions.assertThat(response.getIsSuccessful()).isTrue();
    }

    @Test
    public void givenDebitCardIdAndUpdateDebitCardRequest_whenUpdate_thenReturnSuccessfullyResponse() throws DebitCardOperationException, CustomerNotFoundException {
        // given
        DebitCard debitCard = createDebitCard();
        Customer customer = createCustomer();
        UpdateDebitCardRequest request = new UpdateDebitCardRequest();
        request.setPassword("1111");
        request.setDailyLimit(BigDecimal.ZERO);
        given(customerService.findByCustomerById(customer.getId())).willReturn(Optional.of(customer));
        given(debitCardService.findDebitCardWithCustomerIDAndCardID(customer.getId(), debitCard.getId())).willReturn(Optional.of(debitCard));
        // when
        GeneralResponse response = underTest.update(customer.getId(), debitCard.getId(), request);
        // then
        assertThat(response.getIsSuccessful()).isTrue();
    }

    @Test
    public void givenDebitCardId_whenDeleteDebitCardById_thenReturnSuccessfullyResponse() throws DebitCardOperationException, CustomerNotFoundException {
        // given
        DebitCard debitCard = createDebitCard();
        CheckingAccount checkingAccount = createCheckingAccount();
        checkingAccount.setBalance(BigDecimal.ZERO);
        debitCard.setCheckingAccount(checkingAccount);
        Customer customer = createCustomer();
        given(customerService.findByCustomerById(customer.getId())).willReturn(Optional.of(customer));
        given(debitCardService.findDebitCardWithCustomerIDAndCardID(customer.getId(), debitCard.getId())).willReturn(Optional.of(debitCard));
        // when
        GeneralResponse response = underTest.deleteDebitCardById(customer.getId(), debitCard.getId());
        // then
        assertThat(response.getIsSuccessful()).isTrue();
    }

    @Test
    public void givenHasbalanceDebitCard_whenDeleteDebitCardById_thenThrowsDebitCardOperationException() throws DebitCardOperationException, CustomerNotFoundException {
        // given
        DebitCard debitCard = createDebitCard();
        CheckingAccount checkingAccount = createCheckingAccount();
        checkingAccount.setBalance(BigDecimal.TEN);
        debitCard.setCheckingAccount(checkingAccount);
        Customer customer = createCustomer();
        given(customerService.findByCustomerById(customer.getId())).willReturn(Optional.of(customer));
        given(debitCardService.findDebitCardWithCustomerIDAndCardID(customer.getId(), debitCard.getId())).willReturn(Optional.of(debitCard));
        // when
        assertThrows(DebitCardOperationException.class, () -> underTest.deleteDebitCardById(customer.getId(), debitCard.getId()));
        // then
    }
    /*
        @Test
    public void givenInvalidAccountId_whenBlockAccount_thenThrowsCheckingAccountNotFoundException() throws CustomerNotFoundException, AccountNameAlreadyHaveException {

        // given
        Long accountId = 1L;
        BDDMockito.given(checkingAccountService.findById(accountId)).willReturn(Optional.empty());
        // then
        assertThrows(CheckingAccountNotFoundException.class, () -> underTest.blockAccount(accountId));
        Mockito.verify(checkingAccountService,times(0)).save(createCheckingAccount());

    }
     */

    private CheckingAccount createCheckingAccount() {

        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNo("1111");
        checkingAccount.setId(1L);
        checkingAccount.setBranchCode("00");
        checkingAccount.setBlocked(false);
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("account name");
        checkingAccount.setBranchName("FATIH");
        return checkingAccount;
    }

    private DebitCard createDebitCard() {
        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber("1111");
        debitCard.setId(1L);
        debitCard.setPassword("1111");
        debitCard.setCvv("1111");
        debitCard.setStatus(CardStatus.ACTIVE);
        debitCard.setExpiryDate(new Date());
        return debitCard;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setIdentityNumber("111111111111");
        customer.setId(1L);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        return customer;
    }
}