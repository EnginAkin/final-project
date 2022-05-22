package org.norma.finalproject.account.core.mapper.impl;

import org.norma.finalproject.account.core.mapper.CheckingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.springframework.stereotype.Component;

@Component
public class CheckingAccountMapperImpl implements CheckingAccountMapper {
    @Override
    public CheckingAccount ToEntity(CreateCheckingAccountRequest createCheckingAccountRequest) {
        return CheckingAccount.builder()
                .accountName(createCheckingAccountRequest.getBranchName()+"-"+createCheckingAccountRequest.getBranchCode())
                .branchCode(createCheckingAccountRequest.getBranchCode())
                .branchName(createCheckingAccountRequest.getBranchName())
                .currencyType(createCheckingAccountRequest.getCurrencyType()).build();
    }
    @Override
    public CreateDepositAccountResponse toDto(CheckingAccount checkingAccount) {
        return CreateDepositAccountResponse.builder().accountNo(checkingAccount.getAccountNo())
                .accountName(checkingAccount.getAccountName()).ibanNo(checkingAccount.getIbanNo()).build();
    }
}
