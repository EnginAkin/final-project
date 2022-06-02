package org.norma.finalproject.transfer.service.base;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
public abstract class TransferBase<T> {
    private final BaseAccountService accountService;
    private final FacadeExchangeService exchangeService;

    public abstract GeneralResponse transfer(long customerId, T request) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException, DebitCardNotFoundException;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sendTransferWithIban(String fromAccountIban, String toAccountIban, BigDecimal amount, String description) throws AmountNotValidException, TransferOperationException {

        Account fromAccount = accountService.findAccountByIbanNumber(fromAccountIban).get();
        Account toAccount = accountService.findAccountByIbanNumber(toAccountIban).get();


        fromAccount.setLockedBalance(amount);
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountService.update(fromAccount); // lock balance for security

        BigDecimal checkLockedBalance=fromAccount.getBalance().subtract(fromAccount.getLockedBalance());
        if(checkLockedBalance.compareTo(BigDecimal.ZERO)<0){ //
            fromAccount.setLockedBalance(BigDecimal.ZERO);
            throw new TransferOperationException("Locked balance rather than account balance.");
        }

        AccountActivity fromAccountActivity = new AccountActivity();
        fromAccountActivity.setAccount(fromAccount);
        fromAccountActivity.setCrossAccount(toAccount.getIbanNo());
        fromAccountActivity.setActionStatus(ActionStatus.OUTGOING);
        fromAccountActivity.setAmount(amount);
        fromAccountActivity.setDate(new Date());

        amount = exchangeService.getExchangedAmount(toAccount.getCurrencyType(), fromAccount.getCurrencyType(), amount); // get exchange

        AccountActivity toAccountActivity = new AccountActivity();
        toAccountActivity.setAccount(toAccount);
        toAccountActivity.setCrossAccount(fromAccount.getIbanNo());
        toAccountActivity.setActionStatus(ActionStatus.INCOMING);
        toAccountActivity.setDate(new Date());
        toAccountActivity.setDescription(description);
        toAccountActivity.setAmount(amount);
        toAccount.setBalance(toAccount.getBalance().add(amount));

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
