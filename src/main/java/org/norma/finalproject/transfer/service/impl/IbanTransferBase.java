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
import org.norma.finalproject.transfer.core.model.request.CreateIbanTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.norma.finalproject.transfer.service.TransferService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component("transfer-iban")
public class IbanTransferBase extends TransferBase<CreateIbanTransferRequest> {

    private final CustomerService customerService;
    private final BaseAccountService accountService;

    private final TransferService transferService;

    private final TransferMapper transferMapper;
    private final FacadeExchangeService exchangeService;

    public IbanTransferBase(CustomerService customerService, BaseAccountService accountService, TransferService transferService, TransferMapper transferMapper, FacadeExchangeService exchangeService) {
        super(accountService,exchangeService);
        this.customerService=customerService;
        this.accountService=accountService;
        this.transferService=transferService;
        this.transferMapper=transferMapper;
        this.exchangeService = exchangeService;
    }

    @Override
    @Transactional
    public GeneralResponse transfer(long customerId, CreateIbanTransferRequest transferRequest) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        // check from iban owners
        Optional<Account> optionalFromAccount = accountService.findAccountByIbanNumberAndCustomerId(transferRequest.getFromIban(), customerId);
        if (optionalFromAccount.isEmpty()) {
            throw new TransferOperationException("Customer dont have " + transferRequest.getFromIban() + " Account ");
        }
        // gondericinin hesabında yeterli para var mı ?
        if (optionalFromAccount.get().getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new TransferOperationException("There is not enough money in the account");

        }
        // to iban var mı
        Optional<Account> optionalToAccount = accountService.findAccountByIbanNumber(transferRequest.getToIban());
        if (optionalToAccount.isEmpty()) {
            throw new TransferOperationException("Cross account not found");
        }
        // birikimli hesaptan başka hesaba transfer yapamaz.
        if (optionalFromAccount.get().getAccountType().equals(AccountType.SAVING)){
            if(optionalCustomer.get().getCheckingAccounts().stream().noneMatch(checkingAccount -> checkingAccount.getIbanNo().equals(optionalToAccount.get().getIbanNo()))){
                throw new TransferOperationException("You can transfer only parent checking account account.");
            }
        }
        // transfer
        sendTransfer(optionalFromAccount.get(), optionalToAccount.get(), transferRequest.getAmount(),transferRequest.getDescription());


        Transfer transfer = transferMapper.toEntity(transferRequest);
        transfer.setCurrencyType(optionalFromAccount.get().getCurrencyType());
        transferService.save(transfer);
        return new GeneralSuccessfullResponse("Transfer successfull.");
    }







}
