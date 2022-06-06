package org.norma.finalproject.common.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.card.core.exception.CreditCardNotFoundException;
import org.norma.finalproject.card.core.exception.CreditCardOperationException;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.entity.ExtractOfCard;
import org.norma.finalproject.card.entity.base.CreditCardActivity;
import org.norma.finalproject.card.service.CreditCardService;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.norma.finalproject.common.shop.core.model.request.DoShoppingRequestWithCreditCard;
import org.norma.finalproject.common.shop.core.model.request.DoShoppingRequestWithDebitCard;
import org.norma.finalproject.common.shop.service.ShoppingService;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {

    private final DebitCardService debitCardService;
    private final CreditCardService creditCardService;
    private final BaseAccountService accountService;


    @Override
    public GeneralResponse shoppingWithDebitCard(DoShoppingRequestWithDebitCard doShoppingRequestWithDebitCard) throws DebitCardNotFoundException, DebitCardOperationException, AmountNotValidException, CustomerNotFoundException, TransferOperationException {

        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCardNumber(doShoppingRequestWithDebitCard.getCardNumber());
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardNotFoundException();
        }
        DebitCard debitCard = optionalDebitCard.get();
        if (!(doShoppingRequestWithDebitCard.getCardPassword().equals(debitCard.getPassword()))) {
            throw new DebitCardOperationException("Password invalid.");
        }
        if (debitCard.getCheckingAccount().getBalance().compareTo(doShoppingRequestWithDebitCard.getShoppingAmount()) < 0) {
            throw new DebitCardOperationException("Card balance not enough for this shopping.");
        }

        debitCard.getCheckingAccount().setBalance(debitCard.getCheckingAccount().getBalance().subtract(doShoppingRequestWithDebitCard.getShoppingAmount()));

        AccountActivity accountActivity = new AccountActivity();
        accountActivity.setAccount(debitCard.getCheckingAccount());
        accountActivity.setDescription("Shopping scenario");
        accountActivity.setCrossAccount("Shopping scenario");
        accountActivity.setAmount(doShoppingRequestWithDebitCard.getShoppingAmount());
        accountActivity.setDate(new Date());
        accountActivity.setActionStatus(ActionStatus.OUTGOING);
        accountActivity.setAvailableBalance(debitCard.getCheckingAccount().getBalance());

        debitCard.getCheckingAccount().addActivity(accountActivity);

        accountService.update(debitCard.getCheckingAccount());
        return new GeneralSuccessfullResponse("Shopping successfull.");
    }

    @Override
    @Transactional
    public GeneralResponse shoppingWithCreditCard(DoShoppingRequestWithCreditCard shoppingRequest) throws CreditCardNotFoundException, CreditCardOperationException {

        Optional<CreditCard> optionalCreditCard = creditCardService.findCreditCardByCreditCardNumber(shoppingRequest.getCardNumber());
        if (optionalCreditCard.isEmpty()) {
            throw new CreditCardNotFoundException();
        }
        CreditCard creditCard = optionalCreditCard.get();
        if (!(shoppingRequest.getCardPassword().equals(creditCard.getPassword()))) {
            throw new CreditCardOperationException("Password invalid.");
        }
        if (creditCard.getCreditCardAccount().getAvailableBalance().compareTo(shoppingRequest.getShoppingAmount()) < 0) {
            throw new CreditCardOperationException("Not enough available balance for shopping. please upgrade credit limit.");
        }
        // ekstre donemi
        ExtractOfCard currentTermExtract = creditCard.getCreditCardAccount().getCurrentTermExtract();
        CreditCardActivity activity = new CreditCardActivity();
        activity.setAmount(shoppingRequest.getShoppingAmount());
        activity.setDescription(shoppingRequest.getDescription());
        activity.setCrossAccount("Shopping");
        activity.setProcessDate(new Date());
        activity.setExtractOfCard(currentTermExtract);
        activity.setSpendCategory(shoppingRequest.getSpendCategory());

        creditCard.getCreditCardAccount().setTotalDebt(creditCard.getCreditCardAccount().getTotalDebt().add(shoppingRequest.getShoppingAmount()));
        currentTermExtract.addCreditCardActivity(activity);
        currentTermExtract.setCreditCardAccount(creditCard.getCreditCardAccount());
        BigDecimal availableBalance = creditCard.getCreditCardAccount().getAvailableBalance();
        creditCard.getCreditCardAccount().setAvailableBalance(availableBalance.subtract(shoppingRequest.getShoppingAmount()));
        return new GeneralSuccessfullResponse("Shopping successfully completed.");
    }


}
