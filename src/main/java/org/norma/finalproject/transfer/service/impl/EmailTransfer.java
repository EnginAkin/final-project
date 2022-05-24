package org.norma.finalproject.transfer.service.impl;


import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailTransfer extends TransferBase<EmailTransferRequest> {
    private final BaseAccountService accountService;
    private final CustomerService customerService;
    private final FacadeExchangeService exchangeService;

    public EmailTransfer(BaseAccountService accountService, CustomerService customerService, FacadeExchangeService exchangeService) {
        super(accountService,exchangeService);
        this.accountService = accountService;
        this.customerService = customerService;
        this.exchangeService = exchangeService;
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
        Optional<Account> optionalToAccount = accountService.findAccountByEmail(emailTransferRequest.getToEmail());
        if(optionalToAccount.isEmpty()){
            throw new TransferOperationException("Account not found with : "+emailTransferRequest.getFromAccountId());
        }
        if(optionalFromAccount.get().getBalance().compareTo(emailTransferRequest.getAmount())<0){
            throw new TransferOperationException("Account balance not enough for transfer");
        }
        sendTransfer(optionalFromAccount.get(),optionalToAccount.get(),emailTransferRequest.getAmount(),emailTransferRequest.getDescription());
        return null;
    }
}


