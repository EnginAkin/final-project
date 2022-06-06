package org.norma.finalproject.card.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.card.core.exception.CreditCardNotFoundException;
import org.norma.finalproject.card.core.exception.CreditCardOperationException;
import org.norma.finalproject.card.core.mapper.CreditCardMapper;
import org.norma.finalproject.card.core.model.request.CreateCreditCardRequest;
import org.norma.finalproject.card.core.model.response.CreditCardActivityResponse;
import org.norma.finalproject.card.core.model.response.CreditCardDebtResponse;
import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.CreditCardAccount;
import org.norma.finalproject.card.entity.base.CreditCardActivity;
import org.norma.finalproject.card.entity.enums.SpendCategory;
import org.norma.finalproject.card.service.CreditCardService;
import org.norma.finalproject.card.service.CreditLimitCalculator;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CreditCardFacadeServiceImplTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private CreditLimitCalculator<BigDecimal> creditLimitCalculatorByIncome;
    @Mock
    private UniqueNoCreator uniqueNoCreator;
    @Mock
    private CreditCardService creditCardService;
    @Mock
    private CreditCardMapper creditCardMapper;

    @InjectMocks
    private CreditCardFacadeServiceImpl underTest;


    @Test
    public void givenCreateCreditCardRequest_whenCreate_thenReturnGeneralSuccessfullResponse() throws CreditCardOperationException, CustomerNotFoundException {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        CreateCreditCardRequest createCreditCardRequest = new CreateCreditCardRequest();
        createCreditCardRequest.setCreditCardLimit(BigDecimal.valueOf(2000));
        createCreditCardRequest.setPassword("1234");
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditLimitCalculatorByIncome.getCreditLimit(customer.getIncome(), createCreditCardRequest.getCreditCardLimit())).willReturn(BigDecimal.valueOf(1000));
        BDDMockito.given(uniqueNoCreator.creatCardNumber()).willReturn("11111");
        // when
        GeneralResponse response = underTest.create(customerID, createCreditCardRequest);
        // then
        Assertions.assertThat(response.getIsSuccessful()).isTrue();

    }

    @Test
    public void givenCreateCreditCardRequestWithInvalidCustomerID_whenCreate_thenReturnGeneralSuccessfullResponse() throws CreditCardOperationException, CustomerNotFoundException {
        // given
        Long customerID = 1L;
        CreateCreditCardRequest createCreditCardRequest = new CreateCreditCardRequest();
        createCreditCardRequest.setCreditCardLimit(BigDecimal.valueOf(2000));
        createCreditCardRequest.setPassword("1234");
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.empty());
        // when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            underTest.create(customerID, createCreditCardRequest);
        });
        // then
        String expectedExceptionMessage = "Customer not found.";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));

    }

    @Test
    public void givenCreateCreditCardRequestWithInvalidCreditLimitRequestCustomerID_whenCreate_thenReturnGeneralSuccessfullResponse() throws CreditCardOperationException, CustomerNotFoundException {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.ZERO);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        CreateCreditCardRequest createCreditCardRequest = new CreateCreditCardRequest();
        createCreditCardRequest.setCreditCardLimit(BigDecimal.valueOf(2000));
        createCreditCardRequest.setPassword("1234");
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditLimitCalculatorByIncome.getCreditLimit(customer.getIncome(), createCreditCardRequest.getCreditCardLimit())).willReturn(BigDecimal.ZERO);
        // when
        CreditCardOperationException exception = assertThrows(CreditCardOperationException.class, () -> {
            underTest.create(customerID, createCreditCardRequest);
        });
        // then
        String expectedExceptionMessage = "very risky";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));

    }

    @Test
    public void givenCreditCardID_whenCurrentTermTransactions_thenReturnCreditCardTransactions() throws CreditCardOperationException, CustomerNotFoundException, CreditCardNotFoundException {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");

        CreditCardActivity activity = new CreditCardActivity();
        activity.setSpendCategory(SpendCategory.PETROL);
        activity.setProcessDate(new Date());
        activity.setAmount(BigDecimal.TEN);

        CreditCardActivity activity2 = new CreditCardActivity();
        activity2.setSpendCategory(SpendCategory.PETROL);
        activity2.setProcessDate(new Date());
        activity2.setAmount(BigDecimal.TEN);

        CreditCardActivityResponse response = new CreditCardActivityResponse();
        response.setDescription("example");
        response.setAmount(BigDecimal.TEN);
        response.setProcessDate(new Date());

        CreditCard creditCard = new CreditCard();
        Long creditCardId = 1L;
        creditCard.setCustomer(customer);
        creditCard.setId(creditCardId);
        creditCard.setCardNumber("111");
        CreditCardAccount account = new CreditCardAccount();
        creditCard.setCreditCardAccount(account);
        creditCard.getCreditCardAccount().getCurrentTermExtract().setCreditCardActivities(List.of(activity, activity2));

        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditCardService.findCreditCardById(creditCardId)).willReturn(Optional.of(creditCard));
        BDDMockito.given(creditCardMapper.toCreditCardActivityResponse(activity)).willReturn(response);
        BDDMockito.given(creditCardMapper.toCreditCardActivityResponse(activity2)).willReturn(response);
        // when
        GeneralDataResponse transactions = (GeneralDataResponse) underTest.getCurrentTermTransactions(customerID, creditCardId);

        //then
        assertThat(transactions.getData()).isNotNull();
        assertThat(transactions.getIsSuccessful()).isTrue();

    }

    @Test
    public void givenInvalidCustomerId_whenGetCurrentTermTransactions_thenThrowsCustomerNotFoundException() {
        // given
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.empty());

        // when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            underTest.getCurrentTermTransactions(customerID, 1L);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void givenInvalidCreditCardId_whenGetCurrentTermTransactions_thenThrowsCreditCardFoundException() {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        Long invalidCreditCardId = 1L;

        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditCardService.findCreditCardById(invalidCreditCardId)).willReturn(Optional.empty());


        // when
        CreditCardNotFoundException exception = assertThrows(CreditCardNotFoundException.class, () -> {
            underTest.getCurrentTermTransactions(customerID, 1L);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void givenCreditCardOwnerNotCustomer_whenGetCurrentTermTransactions_thenThrowsCreditCardOperationException() {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setIdentityNumber("111111111111");
        customer2.setIncome(BigDecimal.TEN);
        customer2.setName("ENGIN");
        customer2.setSurname("AKIN");

        CreditCard creditCard = new CreditCard();
        Long creditCardId = 1L;
        creditCard.setCustomer(customer2);
        creditCard.setId(creditCardId);
        creditCard.setCardNumber("111");

        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer2));
        BDDMockito.given(creditCardService.findCreditCardById(creditCardId)).willReturn(Optional.of(creditCard));


        // when
        CreditCardOperationException exception = assertThrows(CreditCardOperationException.class, () -> {
            underTest.getCurrentTermTransactions(customerID, creditCardId);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void givenCreditCustomerID_whenGetCustomerCreditCards_thenThrowsGeneralSucessfullResponse() throws CustomerNotFoundException {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");

        CreditCard creditCard = new CreditCard();
        creditCard.setCustomer(customer);
        creditCard.setId(1L);
        creditCard.setCardNumber("111");

        CreditCard creditCard2 = new CreditCard();
        creditCard2.setCustomer(customer);
        creditCard2.setId(1L);
        creditCard2.setCardNumber("111");
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        // when

        GeneralDataResponse creditCards = (GeneralDataResponse<List<CreditCardResponse>>) underTest.getCustomerCreditCards(customerID);

        // then
        Assertions.assertThat(creditCards.getIsSuccessful()).isTrue();
        Assertions.assertThat(creditCards.getData()).isNotNull();
    }

    @Test
    public void givenInvalidCustomerId_whenGetCustomerCreditCards_thenThrowsCustomerNotFoundException() {
        // given
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.empty());

        // when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            underTest.getCustomerCreditCards(customerID);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void givenCreditCardId_whenGetCreditCardDebt_thenReturnCreditCardDebt() throws CreditCardOperationException, CustomerNotFoundException, CreditCardNotFoundException {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");

        CreditCard creditCard = new CreditCard();
        Long creditCardId = 1L;
        creditCard.setCustomer(customer);
        creditCard.setId(creditCardId);
        CreditCardAccount creditCardAccount = new CreditCardAccount();
        creditCardAccount.setTotalDebt(BigDecimal.TEN);
        creditCardAccount.setLastExtractDebt(BigDecimal.TEN);
        creditCard.setCreditCardAccount(creditCardAccount);
        creditCard.setCardNumber("111");

        CreditCardDebtResponse response = new CreditCardDebtResponse();
        response.setTotalDebt(BigDecimal.TEN);
        response.setCurrentTermDebt(BigDecimal.TEN);
        response.setLastExtractDebt(BigDecimal.TEN);

        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditCardService.findCreditCardById(creditCardId)).willReturn(Optional.of(creditCard));

        // when
        GeneralResponse creditCardDebt = underTest.getCreditCardDebt(customerID, creditCardId);
        //then
        Assertions.assertThat(creditCardDebt.getIsSuccessful()).isTrue();

    }

    @Test
    public void givenInvalidCustomerId_whenGetCustomerCreditCardDebt_thenThrowsCustomerNotFoundException() {
        // given
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.empty());

        // when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            underTest.getCustomerCreditCards(customerID);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void givenInvalidCreditCardId_whenGetCreditCardDebts_thenThrowsCreditCardFoundException() {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        Long invalidCreditCardId = 1L;

        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditCardService.findCreditCardById(invalidCreditCardId)).willReturn(Optional.empty());


        // when
        CreditCardNotFoundException exception = assertThrows(CreditCardNotFoundException.class, () -> {
            underTest.getCreditCardDebt(customerID, 1L);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    @Test
    public void givenCreditCard_whenDeleteCreditCard_thenReturnSuccessfullyResponse() throws CreditCardOperationException, CustomerNotFoundException, CreditCardNotFoundException {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");

        CreditCard creditCard = new CreditCard();
        Long creditCardId = 1L;
        CreditCardAccount creditCardAccount = new CreditCardAccount();
        creditCardAccount.setTotalDebt(BigDecimal.ZERO);
        creditCard.setCreditCardAccount(creditCardAccount);
        creditCard.setCustomer(customer);
        creditCard.setId(creditCardId);
        creditCard.setCardNumber("111");
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditCardService.findCreditCardById(creditCardId)).willReturn(Optional.of(creditCard));
        doNothing().when(creditCardService).delete(creditCard);
        // when
        GeneralResponse response = underTest.deleteCreditCard(customerID, creditCardId);
        //then
        Assertions.assertThat(response.getIsSuccessful()).isTrue();

    }

    @Test
    public void givenInvalidCreditCardId_whenDeleteCreditCard_thenThrowsCreditCardFoundException() {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        Long invalidCreditCardId = 1L;

        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditCardService.findCreditCardById(invalidCreditCardId)).willReturn(Optional.empty());


        // when
        CreditCardNotFoundException exception = assertThrows(CreditCardNotFoundException.class, () -> {
            underTest.deleteCreditCard(customerID, 1L);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }


    @Test
    public void givenInvalidCustomerId_whenDeleteCreditCard_thenThrowsCustomerNotFoundException() {
        // given
        Long customerID = 1L;
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.empty());

        // when
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            underTest.deleteCreditCard(customerID, 1L);
        });
        //then
        String expectedExceptionMessage = "not found";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }


    @Test
    public void givenHasADebtCreditCard_whenDeleteCreditCard_thenThrowsCreditCardOperationException() throws CreditCardOperationException, CustomerNotFoundException, CreditCardNotFoundException {
        // given
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");

        CreditCard creditCard = new CreditCard();
        Long creditCardId = 1L;
        CreditCardAccount creditCardAccount = new CreditCardAccount();
        creditCardAccount.setTotalDebt(BigDecimal.TEN);
        creditCard.setCreditCardAccount(creditCardAccount);
        creditCard.setCustomer(customer);
        creditCard.setId(creditCardId);
        creditCard.setCardNumber("111");
        BDDMockito.given(customerService.findByCustomerById(customerID)).willReturn(Optional.of(customer));
        BDDMockito.given(creditCardService.findCreditCardById(creditCardId)).willReturn(Optional.of(creditCard));
        // when
        CreditCardOperationException exception = assertThrows(CreditCardOperationException.class, () -> {
            underTest.deleteCreditCard(customerID, creditCardId);
        });
        //then
        String expectedExceptionMessage = "has a debt";
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));

    }

/*
    @Test
    public void givenCreditCard_whenDeleteCreditCard_thenReturnSuccessfullyResponse(){
        // given
        Long customerID=1L;
        Customer customer=new Customer();
        customer.setId(customerID);
        customer.setIdentityNumber("111111111111");
        customer.setIncome(BigDecimal.TEN);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");

        CreditCard creditCard=new CreditCard();
        Long creditCardId=1L;
        creditCard.setCustomer(customer);
        creditCard.setId(creditCardId);
        CreditCardAccount creditCardAccount=new CreditCardAccount();
        creditCardAccount.setTotalDebt(BigDecimal.TEN);
        creditCardAccount.setLastExtractDebt(BigDecimal.TEN);
        creditCard.setCreditCardAccount(creditCardAccount);
        creditCard.setCardNumber("111");

        // when

        //then

    }
 */
    /*
     @Test
    public void given_when_then(){
        // given

        // when

        //then

    }
     */

}
