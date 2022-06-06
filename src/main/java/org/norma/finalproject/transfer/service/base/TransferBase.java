package org.norma.finalproject.transfer.service.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.exchange.service.FacadeExchangeService;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * temel olarak iban üzerinden transfer yapılırç. farklı transfer yöntemleri implemente edilebilir.
 * email üzerinden transfer yapılmak istenirse transfer abstract metodunu emaile göre şekillendirip kullanıcı ibanları bulunduktan sonra
 * temel sendTransferWithIban metoduna ibanlar gönderilir.
 *
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */

@RequiredArgsConstructor
@Slf4j
public abstract class TransferBase<T> {
    private final BaseAccountService accountService;
    private final FacadeExchangeService exchangeService;


    public abstract GeneralResponse transfer(long customerId, T request) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sendTransferWithIban(String fromAccountIban, String toAccountIban, BigDecimal amount, String description) throws AmountNotValidException, TransferOperationException {
        log.info("transfer started.");
        Account fromAccount = accountService.findAccountByIbanNumber(fromAccountIban).get();
        Account toAccount = accountService.findAccountByIbanNumber(toAccountIban).get();


        fromAccount.setLockedBalance(amount);
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountService.update(fromAccount); // lock balance for security

        BigDecimal checkLockedBalance = fromAccount.getBalance().subtract(fromAccount.getLockedBalance());
        if (checkLockedBalance.compareTo(BigDecimal.ZERO) < 0) {
            return; // locked balance grather than main balance.
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
        log.info("transfer ended.");

    }

}
