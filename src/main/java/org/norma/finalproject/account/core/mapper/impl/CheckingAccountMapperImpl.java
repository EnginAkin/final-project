package org.norma.finalproject.account.core.mapper.impl;

import org.norma.finalproject.account.core.mapper.CheckingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.core.model.response.CheckingAccountResponse;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CheckingAccountMapperImpl implements CheckingAccountMapper {
    @Override
    public CheckingAccount ToEntity(CreateCheckingAccountRequest createCheckingAccountRequest) {

        return CheckingAccount.builder()
                .accountName(createCheckingAccountRequest.getBranchName()+"-"+createCheckingAccountRequest.getBranchCode())
                .branchCode(createCheckingAccountRequest.getBranchCode())
                .branchCode(createCheckingAccountRequest.getBranchCode())
                .bankCode(createCheckingAccountRequest.getBankCode())
                .branchName(createCheckingAccountRequest.getBranchName())
                .createdAt(new Date())
                .createdBy("ENGIN AKIN")
                .currencyType(createCheckingAccountRequest.getCurrencyType()).build();
    }
    @Override
    public CreateDepositAccountResponse toCreateCheckingAccountDto(CheckingAccount checkingAccount) {
        return new CreateDepositAccountResponse(checkingAccount.getAccountName(), checkingAccount.getAccountNo(),checkingAccount.getIbanNo());
    }

    @Override
    public CheckingAccountResponse toAccountResponses(CheckingAccount checkingAccount) {
        CheckingAccountResponse checkingAccountResponse=new CheckingAccountResponse();
        checkingAccountResponse.setBalance(checkingAccount.getBalance());
        checkingAccountResponse.setCurrencyType(checkingAccount.getCurrencyType());
        checkingAccountResponse.setAccountNo(checkingAccount.getAccountNo());
        checkingAccountResponse.setAccountName(checkingAccount.getAccountName());
        checkingAccountResponse.setIbanNo(checkingAccount.getIbanNo());
        return checkingAccountResponse;
    }
}
