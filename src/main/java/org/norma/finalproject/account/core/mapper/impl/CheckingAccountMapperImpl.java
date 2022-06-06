package org.norma.finalproject.account.core.mapper.impl;

import org.norma.finalproject.account.core.mapper.CheckingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.core.model.response.CheckingAccountResponse;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CheckingAccountMapperImpl implements CheckingAccountMapper {
    @Override
    public CheckingAccount toEntity(CreateCheckingAccountRequest createCheckingAccountRequest) {
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountName(createCheckingAccountRequest.getBranchName() + "-" + createCheckingAccountRequest.getBranchCode());
        checkingAccount.setBranchCode(createCheckingAccountRequest.getBranchCode());
        checkingAccount.setBankCode(createCheckingAccountRequest.getBankCode());
        checkingAccount.setBranchName(createCheckingAccountRequest.getBranchName());
        checkingAccount.setCreatedAt(new Date());
        checkingAccount.setCreatedBy("ENGIN AKIN");
        checkingAccount.setCurrencyType(createCheckingAccountRequest.getCurrencyType());
        checkingAccount.setAccountType(AccountType.CHECKING);
        return checkingAccount;


    }

    @Override
    public CreateDepositAccountResponse toCreateCheckingAccountDto(CheckingAccount checkingAccount) {
        return new CreateDepositAccountResponse(checkingAccount.getAccountName(), checkingAccount.getAccountNo(), checkingAccount.getIbanNo());
    }

    @Override
    public CheckingAccountResponse toAccountResponses(CheckingAccount checkingAccount) {

        CheckingAccountResponse checkingAccountResponse = new CheckingAccountResponse();
        checkingAccountResponse.setId(checkingAccount.getId());
        checkingAccountResponse.setBalance(checkingAccount.getBalance());
        checkingAccountResponse.setCurrencyType(checkingAccount.getCurrencyType());
        checkingAccountResponse.setAccountNo(checkingAccount.getAccountNo());
        checkingAccountResponse.setAccountName(checkingAccount.getAccountName());
        checkingAccountResponse.setIbanNo(checkingAccount.getIbanNo());
        return checkingAccountResponse;
    }
}
