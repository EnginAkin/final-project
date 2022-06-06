package org.norma.finalproject.common.atm.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.mapper.DebitCardMapper;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.common.atm.model.request.DepositRequest;
import org.norma.finalproject.common.atm.model.request.WithdrawRequest;
import org.norma.finalproject.common.atm.service.AtmService;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * for deposit and withdraw money
 *
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
public class AtmServiceImpl implements AtmService {

    private final DebitCardService debitCardService;
    private final BaseAccountService accountService;

    private final DebitCardMapper debitCardMapper;


    @Override
    public GeneralResponse depositToCard(DepositRequest depositRequest) throws DebitCardNotFoundException, DebitCardOperationException {
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCardNumber(depositRequest.getCardNumber());
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardNotFoundException();
        }
        if (!(optionalDebitCard.get().getPassword().equals(depositRequest.getCardPassword()))) {
            throw new DebitCardOperationException("Debit card password not valid.");
        }
        optionalDebitCard.get().getCheckingAccount().setBalance(optionalDebitCard.get().getCheckingAccount().getBalance().add(depositRequest.getDepositAmount()));
        AccountActivity activity = new AccountActivity();
        activity.setActionStatus(ActionStatus.INCOMING);
        activity.setDate(new Date());
        activity.setAccount(optionalDebitCard.get().getCheckingAccount());
        activity.setDescription("Deposit with atm");
        activity.setAmount(depositRequest.getDepositAmount());
        activity.setCrossAccount("ATM");
        activity.setAvailableBalance(optionalDebitCard.get().getCheckingAccount().getBalance());

        optionalDebitCard.get().getCheckingAccount().addActivity(activity);
        accountService.update(optionalDebitCard.get().getCheckingAccount());
        DebitCardResponse response = debitCardMapper.toDto(optionalDebitCard.get());
        return new GeneralDataResponse<>(response, "Deposit successfully completed.");
    }

    @Override
    public GeneralResponse withdrawFromCard(WithdrawRequest withdrawRequest) throws DebitCardNotFoundException, DebitCardOperationException {
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCardNumber(withdrawRequest.getCardNumber());
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardNotFoundException();
        }
        if (!(optionalDebitCard.get().getPassword().equals(withdrawRequest.getCardPassword()))) {
            throw new DebitCardOperationException("Debit card password not valid.");
        }
        if (optionalDebitCard.get().getCheckingAccount().getBalance().compareTo(withdrawRequest.getWithdrawAmount()) < 0) {
            throw new DebitCardOperationException("Not enough money for withdraw.");
        }

        optionalDebitCard.get().getCheckingAccount().setBalance(optionalDebitCard.get().getCheckingAccount().getBalance().subtract(withdrawRequest.getWithdrawAmount()));

        AccountActivity activity = new AccountActivity();
        activity.setActionStatus(ActionStatus.OUTGOING);
        activity.setDate(new Date());
        activity.setAccount(optionalDebitCard.get().getCheckingAccount());
        activity.setDescription("withdraw in atm");
        activity.setAmount(withdrawRequest.getWithdrawAmount());
        activity.setCrossAccount("Atm for user");
        activity.setAvailableBalance(optionalDebitCard.get().getCheckingAccount().getBalance());


        optionalDebitCard.get().getCheckingAccount().addActivity(activity);
        accountService.update(optionalDebitCard.get().getCheckingAccount());
        DebitCardResponse response = debitCardMapper.toDto(optionalDebitCard.get());
        return new GeneralDataResponse<>(response, "withdraw successfully completed.");


    }
}
