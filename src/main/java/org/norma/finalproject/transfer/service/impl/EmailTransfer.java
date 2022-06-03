package org.norma.finalproject.transfer.service.impl;


import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.mapper.TransferMapper;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.service.TransferService;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("transfer-email")
public class EmailTransfer extends TransferBase<EmailTransferRequest> {
    private final BaseAccountService accountService;
    private final CustomerService customerService;
    private final FacadeExchangeService exchangeService;
    private final CheckingAccountService checkingAccountService;
    private final TransferMapper transferMapper;
    private final TransferService transferService;

    public EmailTransfer(BaseAccountService accountService, CustomerService customerService, FacadeExchangeService exchangeService, CheckingAccountService checkingAccountService, TransferMapper transferMapper, TransferService transferService) {
        super(accountService, exchangeService);
        this.accountService = accountService;
        this.customerService = customerService;
        this.exchangeService = exchangeService;
        this.checkingAccountService = checkingAccountService;
        this.transferMapper = transferMapper;
        this.transferService = transferService;
    }

    @Override
    public GeneralResponse transfer(long customerId, EmailTransferRequest emailTransferRequest) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<Account> optionalFromAccount = accountService.findAccountByIbanNumber(emailTransferRequest.getFromAccountIban());
        if (optionalFromAccount.isEmpty()) {
            throw new TransferOperationException("Sender account not found.");
        }
        boolean fromAccountIDOwnerIsCustomer = checkAccountIDOwnerIsCustomer(optionalCustomer.get(), optionalFromAccount.get().getId());
        if (!fromAccountIDOwnerIsCustomer) {
            throw new TransferOperationException("Account not found.");
        }
        Optional<CheckingAccount> optionalToAccount = checkingAccountService.findCheckingAccountByEmail(emailTransferRequest.getToEmail());
        if (optionalToAccount.isEmpty()) {
            throw new TransferOperationException(emailTransferRequest.getToEmail() + " Not found.");
        }

        if (optionalFromAccount.get().getBalance().compareTo(emailTransferRequest.getAmount()) < 0) {
            throw new TransferOperationException("Account balance not enough for transfer");
        }
        // call base method
        this.sendTransferWithIban(optionalFromAccount.get().getIbanNo(), optionalToAccount.get().getIbanNo(), emailTransferRequest.getAmount(), emailTransferRequest.getDescription());

        Transfer transfer = transferMapper.toEntity(new IbanTransferRequest(optionalFromAccount.get().getIbanNo(),optionalToAccount.get().getIbanNo(),emailTransferRequest.getAmount(),emailTransferRequest.getDescription(),emailTransferRequest.getTransferType()));
        transferService.save(transfer);
        return new GeneralSuccessfullResponse("Transfer successfully.");
    }

    private boolean checkAccountIDOwnerIsCustomer(Customer customer, long accountId) {
        return customer.getSavingAccounts().stream()
                        .anyMatch(savingAccount -> savingAccount.getId().equals(accountId)) ||
                customer.getCheckingAccounts().stream()
                        .anyMatch(checkingAccount -> checkingAccount.getId().equals(accountId));
    }


}


