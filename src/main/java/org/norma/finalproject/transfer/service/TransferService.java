package org.norma.finalproject.transfer.service;

import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;

public interface TransferService <T>{

    GeneralResponse transfer(long customerId,T data) throws CustomerNotFoundException;
}
