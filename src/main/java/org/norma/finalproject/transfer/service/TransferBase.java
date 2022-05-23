package org.norma.finalproject.transfer.service;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
public abstract class TransferBase<T>{
    private final BaseAccountService accountService;
    public abstract GeneralResponse transfer(long customerId, T request) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendTransfer(Account fromAccount, Account toAccount, BigDecimal amount, String description) throws AmountNotValidException {
        fromAccount.setLockedBalance(amount);
        accountService.update(fromAccount); // lock balance for security
        BigDecimal exchangedAmount = BigDecimal.ZERO;
/*
        if(fromAccount.getCurrencyType()!=toAccount.getCurrencyType()){
            exchangedAmount= exchangeService.getExchangedAmount(toAccount.getCurrencyType(), fromAccount.getCurrencyType(), amount);

        }
    // gelen birimi bire setle!!
    // id dön
 */
        AccountActivity toAccountActivity = new AccountActivity();
        toAccountActivity.setAccount(toAccount);
        toAccountActivity.setCrossAccount(fromAccount.getIbanNo());
        toAccountActivity.setActionStatus(ActionStatus.INCOMING);
        toAccountActivity.setDate(new Date());
        toAccountActivity.setDescription(description);

        AccountActivity fromAccountActivity = new AccountActivity();
        fromAccountActivity.setAccount(fromAccount);
        fromAccountActivity.setCrossAccount(toAccount.getIbanNo());
        fromAccountActivity.setActionStatus(ActionStatus.OUTGOING);
        fromAccountActivity.setAmount(amount);
        fromAccountActivity.setDate(new Date());

        if(exchangedAmount.compareTo(BigDecimal.ZERO)==0){
            toAccountActivity.setAmount(amount);
            toAccount.setBalance(toAccount.getBalance().add(amount));
        }else{
            toAccountActivity.setAmount(exchangedAmount);
            toAccount.setBalance(toAccount.getBalance().add(exchangedAmount));
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));

        toAccountActivity.setAvailableBalance(toAccount.getBalance());
        fromAccountActivity.setAvailableBalance(fromAccount.getBalance());

        // add activity account
        toAccount.addActivity(toAccountActivity);
        fromAccount.addActivity(fromAccountActivity);

        fromAccount.setLockedBalance(BigDecimal.ZERO);
        accountService.update(toAccount);
        accountService.update(fromAccount);

    }

}
