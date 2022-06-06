package org.norma.finalproject.account.core.mapper;

import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.core.model.response.CheckingAccountResponse;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.entity.CheckingAccount;

public interface CheckingAccountMapper {


    CheckingAccount toEntity(CreateCheckingAccountRequest createCheckingAccountRequest);

    CreateDepositAccountResponse toCreateCheckingAccountDto(CheckingAccount checkingAccount);

    CheckingAccountResponse toAccountResponses(CheckingAccount checkingAccount);


}
