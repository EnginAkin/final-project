package org.norma.finalproject.transfer.service;

import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;

public interface TransferAdapter<T>{

    GeneralResponse transfer(long customerId,T request) throws CustomerNotFoundException, TransferOperationException;
}
