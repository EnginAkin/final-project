package org.norma.finalproject.common.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.service.SavingAccountService;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.ExtractOfCard;
import org.norma.finalproject.card.service.CreditCardService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * It gives credit card new extract which extract of day in today and Saving
 * account interest every night at 23:59.
 *
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledJob {

    private final SavingAccountService savingAccountService;
    private final CreditCardService creditCardService;


    @Scheduled(cron = "0 59 23 * * *")// every day 23:59 ::00 given interest saving account.
    @Transactional
    public void performTaskUsingCronGivenInterestToSavingAccount() {
        log.debug("Saving account scheduled task started.");
        List<SavingAccount> maturityDateInTodaySavingAccount = savingAccountService.getAllByMaturityDateInToday();
        if (!maturityDateInTodaySavingAccount.isEmpty()) {
            maturityDateInTodaySavingAccount.forEach(this::savingAccountInterestProcess);
        }
        log.debug("Saving account scheduled task ended.");
    }

    @Scheduled(cron = "0 59 23 * * *")
    @Transactional
    public void performTaskUsingCronExtractOfCreditCardProcess() {
        log.debug("Credit card extract scheduled task started.");
        List<CreditCard> allCreditCardCutoffDateInToday = creditCardService.findAllCreditCardsCutoffDateInToday();
        if (!allCreditCardCutoffDateInToday.isEmpty()) {
            allCreditCardCutoffDateInToday.forEach(this::creditCardExtractOfProcess);
        }
        log.debug("Credit card extract scheduled task ended.");
    }

    private void creditCardExtractOfProcess(CreditCard creditCard) {
        creditCard.getCreditCardAccount().getCurrentTermExtract().setCurrentTerm(false); // current term false
        Calendar nextCutOffTerm = Calendar.getInstance();
        nextCutOffTerm.add(Calendar.DAY_OF_MONTH, 30);
        creditCard.getCreditCardAccount().setCutOffDate(nextCutOffTerm.getTime());
        creditCard.getCreditCardAccount().setLastExtractDebt(creditCard.getCreditCardAccount().getTotalDebt());
        creditCard.getCreditCardAccount().setAvailableBalance(creditCard.getCreditCardAccount().getTotalCreditLimit());
        ExtractOfCard extract = new ExtractOfCard();
        extract.setExtractTerm(new Date());
        extract.setCurrentTerm(true);
        extract.setCreditCardAccount(creditCard.getCreditCardAccount());
        creditCard.getCreditCardAccount().getExtracts().add(extract);
    }

    private void savingAccountInterestProcess(SavingAccount savingAccount) {
        BigDecimal balance = savingAccount.getBalance();
        int period = savingAccount.getMaturity().getValue() / 30; // convert month
        double interest = CalculateInterest(period, balance.doubleValue());
        savingAccount.setBalance(balance.add(BigDecimal.valueOf(interest)));
        // next maturity date
        Calendar nextMaturity = Calendar.getInstance();
        nextMaturity.add(Calendar.DAY_OF_MONTH, savingAccount.getMaturity().getValue());
        savingAccount.setMaturityDate(nextMaturity.getTime());
    }

    private double CalculateInterest(int period, double principal) {
        double monthlyInterestRate = 2.65;
        double multiplier = Math.pow(1.0 + monthlyInterestRate / 100.0, period) - 1.0;
        return multiplier * principal;
    }


}
