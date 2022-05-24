package org.norma.finalproject.transfer.service.impl;

import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.service.TransferBase;
import org.springframework.stereotype.Service;
/*
@Service
public class EmailTransfer extends TransferBase<EmailTransferRequest> {


    public EmailTransfer(BaseAccountService accountService) {
        super(accountService);
    }

    @Override
    public GeneralResponse transfer(long customerId, EmailTransferRequest request) throws CustomerNotFoundException, TransferOperationException, AmountNotValidException {
        return null;
    }
}


 */