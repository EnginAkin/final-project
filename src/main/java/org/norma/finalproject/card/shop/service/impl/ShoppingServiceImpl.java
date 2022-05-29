package org.norma.finalproject.card.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.model.request.DoShoppingRequest;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.card.shop.service.ShoppingService;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.entity.enums.TransferType;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {

    private final DebitCardService debitCardService;

    private final TransferBase<IbanTransferRequest> ibanTransferService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
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

        IbanTransferRequest ibanTransferRequest=new IbanTransferRequest();
        ibanTransferRequest.setToIban(doShoppingRequest.getToIbanNumber());// this is scenario for shopping cross account.
        ibanTransferRequest.setFromIban(optionalDebitCard.get().getCheckingAccount().getIbanNo());
        ibanTransferRequest.setDescription("Shopping scenario");
        ibanTransferRequest.setAmount(doShoppingRequest.getShoppingAmount());
        ibanTransferRequest.setTransferType(TransferType.SHOPPING);

        long customerID=optionalDebitCard.get().getCheckingAccount().getCustomer().getId();

        ibanTransferService.transfer(customerID,ibanTransferRequest);
        optionalDebitCard.get().refreshBalance(); // card balance refresh.
        return new GeneralSuccessfullResponse("Payment successfully.");
    }




}
