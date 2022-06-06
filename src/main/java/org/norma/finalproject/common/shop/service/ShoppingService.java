package org.norma.finalproject.common.shop.service;

import org.norma.finalproject.card.core.exception.CreditCardNotFoundException;
import org.norma.finalproject.card.core.exception.CreditCardOperationException;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.shop.core.model.request.DoShoppingRequestWithCreditCard;
import org.norma.finalproject.common.shop.core.model.request.DoShoppingRequestWithDebitCard;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;

public interface ShoppingService {
    GeneralResponse shoppingWithDebitCard(DoShoppingRequestWithDebitCard doShoppingRequestWithDebitCard) throws DebitCardNotFoundException, DebitCardOperationException, AmountNotValidException, CustomerNotFoundException, TransferOperationException;

    GeneralResponse shoppingWithCreditCard(DoShoppingRequestWithCreditCard shoppingRequest) throws CreditCardNotFoundException, CreditCardOperationException;
}
