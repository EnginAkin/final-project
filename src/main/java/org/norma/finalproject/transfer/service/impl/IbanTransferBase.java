package org.norma.finalproject.transfer.service.impl;

import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.mapper.TransferMapper;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.norma.finalproject.transfer.service.TransferService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component("transfer-iban")
public class IbanTransferBase extends TransferBase<IbanTransferRequest> {

    private final CustomerService customerService;
    private final BaseAccountService accountService;

    private final TransferService transferService;

    private final TransferMapper transferMapper;
    private final FacadeExchangeService exchangeService;

    public IbanTransferBase(CustomerService customerService, BaseAccountService accountService, TransferService transferService, TransferMapper transferMapper, FacadeExchangeService exchangeService) {
        super(accountService, exchangeService);
        this.customerService = customerService;
        this.accountService = accountService;
        this.transferService = transferService;
        this.transferMapper = transferMapper;
        this.exchangeService = exchangeService;
    }

    @Override
    @Transactional
    public GeneralResponse transfer(long customerId, IbanTransferRequest transferRequest) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<Account> optionalFromAccount = accountService.findAccountByIbanNumber(transferRequest.getFromIban());
        if (optionalFromAccount.isEmpty()) {
            throw new TransferOperationException("Customer dont have " + transferRequest.getFromIban() + " Account ");
        }
        boolean checkIbanNumberOwnerIsCustomer= checkIbanNumberOwnerIsCustomer(optionalCustomer.get(),optionalFromAccount.get().getIbanNo());
        if(!checkIbanNumberOwnerIsCustomer){
            throw new TransferOperationException("Customer not owner " + transferRequest.getFromIban() + " Account ");
        }
        if (optionalFromAccount.get().getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new TransferOperationException("There is not enough money in the account");

        }
        Optional<Account> optionalToAccount = accountService.findAccountByIbanNumber(transferRequest.getToIban());
        if (optionalToAccount.isEmpty()) {
            throw new TransferOperationException("Cross account not found");
        }
        // allowed saving -> checking  --- saving-saving not allowed
        if (optionalFromAccount.get().getAccountType().equals(AccountType.SAVING)) {
            if (!(optionalToAccount.get().getAccountType().equals(AccountType.CHECKING))) {
                throw new TransferOperationException("You can transfer only parent checking account.");
            }
        }

        //sendTransfer(optionalFromAccount.get(), optionalToAccount.get(), transferRequest.getAmount(), transferRequest.getDescription());
        sendTransferWithIban(optionalFromAccount.get().getIbanNo(), optionalToAccount.get().getIbanNo(), transferRequest.getAmount(), transferRequest.getDescription());

        return new GeneralSuccessfullResponse("Transfer successfull.");
    }




    private boolean checkIbanNumberOwnerIsCustomer(Customer customer, String ibanNumber) {
        return customer.getSavingAccounts().stream()
                .anyMatch(savingAccount -> savingAccount.getIbanNo().equals(ibanNumber)) ||
                customer.getCheckingAccounts().stream().
                        anyMatch(checkingAccount -> checkingAccount.getIbanNo().equals(ibanNumber));
    }

}
//

/*
    transfer deneme
    2.kişi   10 id   engin3@gmail.com 100 tl  TR3300006101133939763635453          eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTExMTExMTExMiIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NTM0OTY1NzksImlhdCI6MTY1MzQ2NjU3OX0.Y7tyV88t0Tt1fCzd0ax535PL6GUCZt7tKVDyTnQYQI4AQnZixEB5d2-AO5Io9YjnAU4S8dBtVp50vz1XIOjMlg
    1. kişi 9 id TR3300006102444125751110611  engin2@gmail.com
 */
