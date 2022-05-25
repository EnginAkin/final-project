package org.norma.finalproject.transfer.service.impl;


import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("transfer-email")
public class EmailTransfer extends TransferBase<EmailTransferRequest> {
    private final BaseAccountService accountService;
    private final CustomerService customerService;
    private final FacadeExchangeService exchangeService;
    private final CheckingAccountService checkingAccountService;

    public EmailTransfer(BaseAccountService accountService, CustomerService customerService, FacadeExchangeService exchangeService, CheckingAccountService checkingAccountService) {
        super(accountService,exchangeService);
        this.accountService = accountService;
        this.customerService = customerService;
        this.exchangeService = exchangeService;
        this.checkingAccountService = checkingAccountService;
    }

    @Override
    public GeneralResponse transfer(long customerId, EmailTransferRequest emailTransferRequest) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerId);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        Optional<Account> optionalFromAccount = accountService.findById(emailTransferRequest.getFromAccountId());
        if(optionalFromAccount.isEmpty()){
            throw new TransferOperationException("Account not found with : "+emailTransferRequest.getFromAccountId());
        }
        Optional<CheckingAccount> optionalToAccount = checkingAccountService.findCheckingAccountByEmail(emailTransferRequest.getToEmail());
        if(optionalToAccount.isEmpty()){
            throw new TransferOperationException(emailTransferRequest.getToEmail()+" Not found.");
        }

        if(optionalFromAccount.get().getBalance().compareTo(emailTransferRequest.getAmount())<0){
            throw new TransferOperationException("Account balance not enough for transfer");
        }
        //sendTransfer(optionalFromAccount.get(),optionalToAccount.get(),emailTransferRequest.getAmount(),emailTransferRequest.getDescription());
        sendTransferWithIban(optionalFromAccount.get().getIbanNo(),optionalToAccount.get().getIbanNo(),emailTransferRequest.getAmount(),emailTransferRequest.getDescription());
        return null;
    }
}


