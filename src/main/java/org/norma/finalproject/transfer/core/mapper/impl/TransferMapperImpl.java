package org.norma.finalproject.transfer.core.mapper.impl;

import org.norma.finalproject.transfer.core.mapper.TransferMapper;
import org.norma.finalproject.transfer.core.model.request.CreateIbanTransferRequest;
import org.norma.finalproject.transfer.entity.Transfer;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransferMapperImpl implements TransferMapper {

    @Override
    public Transfer toEntity(CreateIbanTransferRequest transferRequest) {
        Transfer transfer=new Transfer();
        transfer.setBalance(transferRequest.getAmount());
        transfer.setDescription(transferRequest.getDescription());
        transfer.setFromIban(transferRequest.getFromIban());

        transfer.setToIban(transferRequest.getToIban());
        transfer.setProcessTime(new Date());
        transfer.setSendType(transferRequest.getSendType());
        return transfer;
    }
}
