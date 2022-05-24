package org.norma.finalproject.account.core.mapper.impl;

import org.norma.finalproject.account.core.mapper.SavingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.core.model.response.CreateSavingAccountResponse;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.customer.core.utilities.Utils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SavingAccountMapperImpl implements SavingAccountMapper {
    @Override
    public SavingAccount createSavingAccountToEntity(CreateSavingAccountRequest createSavingAccountRequest) {
        SavingAccount savingAccount =new SavingAccount();
        savingAccount.setSuccessRate(Utils.calculateSuccessRate(createSavingAccountRequest.getOpeningBalance(), createSavingAccountRequest.getTargetAmount()));
        savingAccount.setMaturity(createSavingAccountRequest.getMaturity());
        savingAccount.setTargetAmount(createSavingAccountRequest.getTargetAmount());
        savingAccount.setPurposeCumulative(createSavingAccountRequest.getPurposeCumulative());
        savingAccount.setAccountName(createSavingAccountRequest.getAccountName());
        savingAccount.setCurrencyType(createSavingAccountRequest.getCurrencyType());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCreatedBy("ENGIN AKIN");
        savingAccount.setAccountType(AccountType.SAVING);
        return savingAccount;
    }

    @Override
    public CreateSavingAccountResponse toCreateSavingAccountDto(SavingAccount savingAccount) {
        CreateSavingAccountResponse response=new CreateSavingAccountResponse();
        response.setIban(savingAccount.getIbanNo());
        response.setId(savingAccount.getId());
        response.setAccountNo(savingAccount.getAccountNo());
        response.setAccountName(savingAccount.getAccountName());
        response.setCurrencyType(savingAccount.getCurrencyType());
        response.setBalance(savingAccount.getBalance());
        response.setParentAccountNumber(savingAccount.getParentAccount().getAccountNo());
        return response;
    }
}
