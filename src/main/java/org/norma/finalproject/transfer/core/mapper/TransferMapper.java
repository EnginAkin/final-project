package org.norma.finalproject.transfer.core.mapper;

import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.service.impl.EmailTransfer;

public interface TransferMapper {

    Transfer toEntity(IbanTransferRequest transferRequest);

}
