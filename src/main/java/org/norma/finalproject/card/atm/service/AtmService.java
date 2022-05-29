package org.norma.finalproject.card.atm.service;

import org.norma.finalproject.card.atm.model.request.DepositRequest;
import org.norma.finalproject.card.atm.model.request.WithdrawRequest;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.common.core.result.GeneralResponse;

public interface AtmService {
    GeneralResponse depositToCard(DepositRequest depositRequest) throws DebitCardNotFoundException, DebitCardOperationException;

    GeneralResponse withdrawFromCard(WithdrawRequest withdrawRequest) throws DebitCardNotFoundException, DebitCardOperationException;
}
