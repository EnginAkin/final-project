package org.norma.finalproject.account.core.mapper.impl;

import org.norma.finalproject.account.core.mapper.SavingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.core.model.response.CreateSavingAccountResponse;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.customer.core.utilities.Utils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SavingAccountMapperImpl implements SavingAccountMapper {
    @Override
    public SavingAccount createSavingAccountToEntity(CreateSavingAccountRequest createSavingAccountRequest) {
        return SavingAccount.builder().
                accountName(createSavingAccountRequest.getAccountName())
                .targetAmount(createSavingAccountRequest.getTargetAmount())
                .purposeCumulative(createSavingAccountRequest.getPurposeCumulative())
                .maturity(createSavingAccountRequest.getMaturity())
                .currencyType(createSavingAccountRequest.getCurrencyType())
                .successRate(Utils.calculateSuccessRate(createSavingAccountRequest.getOpeningBalance(),createSavingAccountRequest.getTargetAmount()))
                .createdAt(new Date())
                .createdBy("ENGIN AKIN")
                .balance(createSavingAccountRequest.getOpeningBalance()).build();
    }

    @Override
    public CreateSavingAccountResponse toCreateSavingAccountDto(SavingAccount savedAccount) {
        CreateSavingAccountResponse response=new CreateSavingAccountResponse();
        response.setAccountNo(savedAccount.getAccountNo());
        response.setAccountName(savedAccount.getAccountName());
        response.setCurrencyType(savedAccount.getCurrencyType());
        response.setBalance(savedAccount.getBalance());
        response.setParentAccountNumber(savedAccount.getParentAccount().getAccountNo());
        return response;
    }
}
