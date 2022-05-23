package org.norma.finalproject.transfer.service.impl;

import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.CreateEmailTransferRequest;
import org.norma.finalproject.transfer.service.TransferAdapter;
import org.springframework.stereotype.Service;

@Service("transfer-email")
public class TransferEmailAdapter implements TransferAdapter<CreateEmailTransferRequest> {


    @Override
    public GeneralResponse transfer(long customerId, CreateEmailTransferRequest request) throws CustomerNotFoundException, TransferOperationException {
        return null;
    }
}
