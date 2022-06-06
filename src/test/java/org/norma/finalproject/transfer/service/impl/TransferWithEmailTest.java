package org.norma.finalproject.transfer.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.mapper.TransferMapper;
import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.entity.enums.TransferType;
import org.norma.finalproject.transfer.service.TransferService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TransferWithEmailTest {
    @Mock
    private BaseAccountService accountService;
    @Mock
    private CustomerService customerService;
    @Mock
    private FacadeExchangeService exchangeService;
    @Mock
    private CheckingAccountService checkingAccountService;
    @Mock
    private TransferMapper transferMapper;
    @Mock
    private TransferService transferService;

    @Spy
    @InjectMocks
    private TransferWithEmail underTest;

    @Test
    public void givenCustomerIdAndEmailTransferRequest_whenTransfer_thenReturnSuccessfully() throws AmountNotValidException, CustomerNotFoundException, TransferOperationException {
        // given
        Long validCustomerId = 1L;
        EmailTransferRequest transferRequest = new EmailTransferRequest();
        transferRequest.setToEmail("enginakin@gmail.com");
        transferRequest.setTransferType(TransferType.OTHER);
        transferRequest.setAmount(BigDecimal.ZERO);
        transferRequest.setDescription("transfer");
        Account fromAccount = createAccount();
        fromAccount.setId(1L);
        fromAccount.setCurrencyType(CurrencyType.TRY);
        transferRequest.setFromAccountIban(fromAccount.getIbanNo());
        CheckingAccount toAccount = new CheckingAccount();
        toAccount.setBalance(BigDecimal.TEN);
        toAccount.setId(2L);
        Customer customer = new Customer();
        String validEmail = "norma.test@gmail.com";
        customer.setEmail(validEmail);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        Transfer transfer = new Transfer();
        BDDMockito.given(customerService.findByCustomerById(validCustomerId)).willReturn(Optional.of(customer));
        BDDMockito.given(accountService.findAccountByIbanNumber(transferRequest.getFromAccountIban())).willReturn(Optional.of(fromAccount));
        BDDMockito.given(underTest.checkAccountIDOwnerIsCustomer(customer, fromAccount.getId())).willReturn(true);
        BDDMockito.given(checkingAccountService.findCheckingAccountByEmail(transferRequest.getToEmail())).willReturn(Optional.of(toAccount));
        doNothing().when(underTest).sendTransferWithIban(fromAccount.getIbanNo(), toAccount.getIbanNo(), transferRequest.getAmount(), transferRequest.getDescription());
        BDDMockito.given(transferMapper.toEntity(any())).willReturn(transfer);
        // when
        GeneralResponse response = underTest.transfer(validCustomerId, transferRequest);
        //
        Assertions.assertThat(response.getIsSuccessful()).isTrue();
    }

    private Account createAccount() {
        Account account = new Account();
        account.setAccountName("account");
        account.setAccountNo("11111111");
        account.setIbanNo("1111111");
        account.setBalance(BigDecimal.TEN);
        account.setCurrencyType(CurrencyType.TRY);
        return account;
    }


}