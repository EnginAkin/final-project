package org.norma.finalproject.transfer.core.mapper.impl;

import org.norma.finalproject.transfer.core.mapper.TransferMapper;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransferMapperImpl implements TransferMapper {

    @Override
    public Transfer toEntity(IbanTransferRequest transferRequest) {
        Transfer transfer = new Transfer();
        transfer.setBalance(transferRequest.getAmount());
        transfer.setDescription(transferRequest.getDescription());
        transfer.setFromIban(transferRequest.getFromIban());
        transfer.setTransferType(transferRequest.getTransferType());

        transfer.setToIban(transferRequest.getToIban());
        transfer.setProcessTime(new Date());
        transfer.setTransferType(transferRequest.getTransferType());
        return transfer;
    }


}
