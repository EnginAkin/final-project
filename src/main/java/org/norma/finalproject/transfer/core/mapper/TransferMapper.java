package org.norma.finalproject.transfer.core.mapper;

import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;

public interface TransferMapper {

    Transfer toEntity(IbanTransferRequest transferRequest);

}
