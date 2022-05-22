package org.norma.finalproject.account.core.mapper;

import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.entity.CheckingAccount;

public interface CheckingAccountMapper {


    CheckingAccount ToEntity(CreateCheckingAccountRequest createCheckingAccountRequest);

    CreateDepositAccountResponse toDto(CheckingAccount checkingAccount);


}
