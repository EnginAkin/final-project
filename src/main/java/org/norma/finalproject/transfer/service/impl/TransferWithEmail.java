package org.norma.finalproject.transfer.service.impl;


import lombok.extern.slf4j.Slf4j;
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
import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.service.TransferService;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Component("transfer-email")
@Slf4j
public class TransferWithEmail extends TransferBase<EmailTransferRequest> {
    private final BaseAccountService accountService;
    private final CustomerService customerService;
    private final FacadeExchangeService exchangeService;
    private final CheckingAccountService checkingAccountService;
    private final TransferMapper transferMapper;
    private final TransferService transferService;

    public TransferWithEmail(BaseAccountService accountService, CustomerService customerService, FacadeExchangeService exchangeService, CheckingAccountService checkingAccountService, TransferMapper transferMapper, TransferService transferService) {
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
        log.debug("Transfer for email proprocessing started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerId);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<Account> optionalFromAccount = accountService.findAccountByIbanNumber(emailTransferRequest.getFromAccountIban());
        if (optionalFromAccount.isEmpty()) {
            log.error("Sender account not found.");
            throw new TransferOperationException("Sender account not found.");
        }
        Account fromAccount = optionalFromAccount.get();
        boolean fromAccountIDOwnerIsCustomer = checkAccountIDOwnerIsCustomer(optionalCustomer.get(), fromAccount.getId());
        if (!fromAccountIDOwnerIsCustomer) {
            log.error("Account not found.");
            throw new TransferOperationException("Account not found.");
        }
        Optional<CheckingAccount> optionalToAccount = checkingAccountService.findCheckingAccountByEmail(emailTransferRequest.getToEmail());
        if (optionalToAccount.isEmpty()) {
            log.error(emailTransferRequest.getToEmail() + " Not found.");
            throw new TransferOperationException(emailTransferRequest.getToEmail() + " Not found.");
        }

        if (fromAccount.getBalance().compareTo(emailTransferRequest.getAmount()) < 0) {
            log.error("Account balance not enough for transfer");
            throw new TransferOperationException("Account balance not enough for transfer");
        }
        // call base method
        this.sendTransferWithIban(fromAccount.getIbanNo(), optionalToAccount.get().getIbanNo(), emailTransferRequest.getAmount(), emailTransferRequest.getDescription());
        Transfer transfer = transferMapper.toEntity(new IbanTransferRequest(fromAccount.getIbanNo(), optionalToAccount.get().getIbanNo(), emailTransferRequest.getAmount(), emailTransferRequest.getDescription(), emailTransferRequest.getTransferType()));
        transfer.setCurrencyType(fromAccount.getCurrencyType());
        transferService.save(transfer);
        log.debug("Transfer for email ended.");

        return new GeneralSuccessfullResponse("Transfer successfully.");
    }

    public boolean checkAccountIDOwnerIsCustomer(Customer customer, long accountId) {
        return customer.getSavingAccounts().stream()
                .anyMatch(savingAccount -> savingAccount.getId().equals(accountId)) ||
                customer.getCheckingAccounts().stream()
                        .anyMatch(checkingAccount -> checkingAccount.getId().equals(accountId));
    }


}


