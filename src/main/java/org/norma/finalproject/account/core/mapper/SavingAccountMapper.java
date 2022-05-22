package org.norma.finalproject.account.core.mapper;

import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.core.model.response.CreateSavingAccountResponse;
import org.norma.finalproject.account.entity.SavingAccount;

public interface SavingAccountMapper {

    SavingAccount createSavingAccountToEntity(CreateSavingAccountRequest createSavingAccountRequest);

    CreateSavingAccountResponse toCreateSavingAccountDto(SavingAccount savedAccount);
}
