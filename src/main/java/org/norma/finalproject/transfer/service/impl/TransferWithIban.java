package org.norma.finalproject.transfer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.card.service.DebitCardService;
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
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.service.TransferService;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Component("transfer-iban")
@Slf4j
public class TransferWithIban extends TransferBase<IbanTransferRequest> {

    private final CustomerService customerService;
    private final BaseAccountService accountService;

    private final TransferService transferService;

    private final TransferMapper transferMapper;
    private final FacadeExchangeService exchangeService;

    public TransferWithIban(CustomerService customerService, BaseAccountService accountService, TransferService transferService, TransferMapper transferMapper, DebitCardService debitCardService, FacadeExchangeService exchangeService) {
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
        log.debug("Transfer iban preprocessing started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerId);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<Account> optionalFromAccount = accountService.findAccountByIbanNumber(transferRequest.getFromIban());
        if (optionalFromAccount.isEmpty()) {
            log.error("Customer dont have " + transferRequest.getFromIban() + " Account ");
            throw new TransferOperationException("Customer dont have " + transferRequest.getFromIban() + " Account ");
        }
        Account fromAccount = optionalFromAccount.get();
        boolean checkIbanNumberOwnerIsCustomer = checkIbanNumberOwnerIsCustomer(optionalCustomer.get(), optionalFromAccount.get().getIbanNo());
        if (!checkIbanNumberOwnerIsCustomer) {
            log.error("Customer not owner " + transferRequest.getFromIban() + " Account ");
            throw new TransferOperationException("Customer not owner " + transferRequest.getFromIban() + " Account ");
        }
        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            log.error("There is not enough money in the account");
            throw new TransferOperationException("There is not enough money in the account");
        }
        Optional<Account> optionalToAccount = accountService.findAccountByIbanNumber(transferRequest.getToIban());

        if (optionalToAccount.isEmpty()) {
            log.error("Cross account not found");
            throw new TransferOperationException("Cross account not found");
        }
        Account toAccount = optionalToAccount.get();

        // allowed saving -> checking And checking ->checking  . Not allowed saving-saving
        if (optionalFromAccount.get().getAccountType().equals(AccountType.SAVING)) {
            if (!(toAccount.getAccountType().equals(AccountType.CHECKING))) {
                log.error("You can transfer only parent checking account.");
                throw new TransferOperationException("You can transfer only parent checking account.");
            }
        }
        // call base iban transfer method.
        this.sendTransferWithIban(fromAccount.getIbanNo(), toAccount.getIbanNo(), transferRequest.getAmount(), transferRequest.getDescription());

        IbanTransferRequest ibanTransferRequest = new IbanTransferRequest(fromAccount.getIbanNo(), toAccount.getIbanNo(), transferRequest.getAmount(), transferRequest.getDescription(), transferRequest.getTransferType());
        Transfer transfer = transferMapper.toEntity(ibanTransferRequest);
        transfer.setCurrencyType(fromAccount.getCurrencyType());
        transferService.save(transfer);
        log.debug("Transfer iban preprocessing ended.");
        return new GeneralSuccessfullResponse("Transfer successfull.");
    }


    private boolean checkIbanNumberOwnerIsCustomer(Customer customer, String ibanNumber) {
        return customer.getSavingAccounts().stream()
                .anyMatch(savingAccount -> savingAccount.getIbanNo().equals(ibanNumber)) ||
                customer.getCheckingAccounts().stream().
                        anyMatch(checkingAccount -> checkingAccount.getIbanNo().equals(ibanNumber));
    }

}
