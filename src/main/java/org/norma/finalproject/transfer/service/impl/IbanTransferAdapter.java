package org.norma.finalproject.transfer.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.CreateIbanTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.service.TransferAdapter;
import org.norma.finalproject.transfer.service.TransferService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service("iban")
@Primary
@RequiredArgsConstructor
public class IbanTransferAdapter<T> implements TransferAdapter<CreateIbanTransferRequest> {

    private final CustomerService customerService;
    private final BaseAccountService accountService;

    private final TransferService transferService;


    @Override
    @Transactional(value = Transactional.TxType.REQUIRED,rollbackOn = Exception.class)
    public GeneralResponse transfer(long customerId, CreateIbanTransferRequest transferRequest) throws CustomerNotFoundException, TransferOperationException {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(customerId);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        // check from iban owners
        Optional<Account> optionalFromAccount = accountService.findAccountByIbanNumberAndCustomerId(transferRequest.getFromIban(),customerId);
        if(optionalCustomer.isEmpty()){
            throw new TransferOperationException("Customer dont have "+transferRequest.getFromIban()+" Account ");
        }
        // gondericinin hesabında yeterli para var mı ?
        if(optionalFromAccount.get().getBalance().compareTo(transferRequest.getAmount())<0){
            throw new TransferOperationException("There is not enough money in the account");

        }
        // to iban var mı
        Optional<Account> optionalToAccount=accountService.findAccountByIbanNumber(transferRequest.getToIban());
        if(optionalToAccount.isEmpty()){
            throw new TransferOperationException("Cross account not found");
        }
        // to iban ile from iban arasında currency sağlanıyor mu
        if(optionalToAccount.get().getCurrencyType()==optionalFromAccount.get().getCurrencyType()){
            sendTransferSameCurrency(optionalFromAccount.get(),optionalToAccount.get(),transferRequest.getAmount());
        }
        // from ibandan para düşülür.
        // to ibana para eklenir.
        // from ibanın hesap haraktlerine ekleni.
        // from ibanın hesap hareketlerine eklenir.

        Transfer transfer=new Transfer();
        transfer.setBalance(transferRequest.getAmount());
        transfer.setDescription(transferRequest.getDescription());
        transfer.setFromIban(transferRequest.getFromIban());
        transfer.setToIban(transferRequest.getToIban());
        transfer.setProcessTime(new Date());
        transfer.setSendType(transferRequest.getSendType());
        transferService.save(transfer);
        return new GeneralSuccessfullResponse("Transfer successfull.");
    }

    @Transactional(value = Transactional.TxType.REQUIRED,rollbackOn = Exception.class)
    public void sendTransferSameCurrency(Account fromAccount, Account toAccount, BigDecimal amount) {
        fromAccount.setLockedBalance(amount);

        AccountActivity toAccountActivity=new AccountActivity();
        toAccountActivity.setAccount(toAccount);
        toAccountActivity.setCrossAccount(fromAccount.getIbanNo());
        toAccountActivity.setActionStatus(ActionStatus.INCOMING);
        toAccountActivity.setAmount(amount);
        toAccountActivity.setDate(new Date());

        toAccountActivity.setDescription("description");

        AccountActivity fromAccountActivity=new AccountActivity();

        fromAccountActivity.setAccount(fromAccount);
        fromAccountActivity.setCrossAccount(toAccount.getIbanNo());
        fromAccountActivity.setActionStatus(ActionStatus.OUTGOING);
        fromAccountActivity.setAmount(amount);
        fromAccountActivity.setDate(new Date());

        fromAccountActivity.setDescription("description");


        toAccount.setBalance(toAccount.getBalance().add(amount));
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccountActivity.setAvailableBalance(toAccount.getBalance());
        fromAccountActivity.setAvailableBalance(fromAccount.getBalance());

        toAccount.addActivity(toAccountActivity);

        fromAccount.addActivity(fromAccountActivity);
        accountService.update(toAccount);
        accountService.update(fromAccount);


    }


}
