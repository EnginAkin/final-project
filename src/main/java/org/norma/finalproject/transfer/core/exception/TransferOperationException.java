package org.norma.finalproject.transfer.core.exception;

import org.norma.finalproject.common.core.exception.BusinessException;

public class TransferOperationException extends BusinessException {
    public TransferOperationException(String message) {
        super(message);
    }
}
