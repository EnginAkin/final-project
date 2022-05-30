package org.norma.finalproject.card.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.card.core.exception.CreditCardNotFoundException;
import org.norma.finalproject.card.core.exception.CreditCardOperationException;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.model.request.DoShoppingRequest;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.entity.base.CreditCardActivity;
import org.norma.finalproject.card.entity.enums.SpendCategory;
import org.norma.finalproject.card.service.CreditCardService;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.card.shop.service.ShoppingService;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.entity.enums.TransferType;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {

    private final DebitCardService debitCardService;
    private final CreditCardService creditCardService;
    private final BaseAccountService accountService;

    @Override
    public GeneralResponse shoppingWithDebitCard(DoShoppingRequest doShoppingRequest) throws DebitCardNotFoundException, DebitCardOperationException, AmountNotValidException, CustomerNotFoundException, TransferOperationException {

        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCardNumber(doShoppingRequest.getCardNumber());
        if(optionalDebitCard.isEmpty()){
            throw new DebitCardNotFoundException();
        }
        if(!(doShoppingRequest.getCardPassword().equals(optionalDebitCard.get().getPassword()))){
            throw new DebitCardOperationException("Password invalid.");
        }
        if(optionalDebitCard.get().getBalance().compareTo(doShoppingRequest.getShoppingAmount())<0){
            throw new DebitCardOperationException("Card balance not enough for this shopping.");
        }

        optionalDebitCard.get().getCheckingAccount().setBalance(optionalDebitCard.get().getBalance().subtract(doShoppingRequest.getShoppingAmount()));

        AccountActivity accountActivity=new AccountActivity();
        accountActivity.setAccount(optionalDebitCard.get().getCheckingAccount());
        accountActivity.setDescription("Shopping scenario");
        accountActivity.setCrossAccount("Shopping scenario");
        accountActivity.setAmount(doShoppingRequest.getShoppingAmount());
        accountActivity.setDate(new Date());
        accountActivity.setActionStatus(ActionStatus.OUTGOING);
        accountActivity.setAvailableBalance(optionalDebitCard.get().getBalance());

        optionalDebitCard.get().getCheckingAccount().addActivity(accountActivity);

        accountService.update(optionalDebitCard.get().getCheckingAccount());
        accountService.refresh(optionalDebitCard.get().getCheckingAccount());
        return new GeneralSuccessfullResponse("Shopping successfull.");
    }

    @Override
    public GeneralResponse shoppingWithCreditCard(DoShoppingRequest doShoppingRequest) throws CreditCardNotFoundException, CreditCardOperationException {

        Optional<CreditCard> optionalCreditCard = creditCardService.findCreditCardByCreditCardNumber(doShoppingRequest.getCardNumber());
        if(optionalCreditCard.isEmpty()){
            throw new CreditCardNotFoundException();
        }
        if(!(doShoppingRequest.getCardPassword().equals(optionalCreditCard.get().getPassword()))){
            throw new CreditCardOperationException("Password invalid.");
        }
        if(optionalCreditCard.get().getCreditCardAccount().getAvailableBalance().compareTo(doShoppingRequest.getShoppingAmount())<0){
            throw new CreditCardOperationException("Not enough available balance for shopping. please upgrade credit limit.");
        }
        /*
        // ekstre donemi
        CreditCardActivity activity=new CreditCardActivity();
        activity.setAmount(doShoppingRequest.getShoppingAmount());
        activity.setDescription("Shopping with credit card");
        activity.setCrossAccount("Shopping");
        activity.setProcessDate(new Date());
        activity.setSpendCategory(SpendCategory.PETROL);
        optionalCreditCard.get().getCreditCardAccount().getCurrentTermTransactions().add(activity);
        BigDecimal availableBalance=optionalCreditCard.get().getCreditCardAccount().getAvailableBalance();
        optionalCreditCard.get().getCreditCardAccount().setAvailableBalance(availableBalance.subtract(doShoppingRequest.getShoppingAmount()));
        creditCardService.save(optionalCreditCard.get());

         */


        return new GeneralSuccessfullResponse("Shopping successfully completed.");
    }


}
