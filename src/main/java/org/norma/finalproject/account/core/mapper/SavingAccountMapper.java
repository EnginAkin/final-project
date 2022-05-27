package org.norma.finalproject.account.core.mapper;

import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.core.model.response.SavingAccountDto;
import org.norma.finalproject.account.entity.SavingAccount;

public interface SavingAccountMapper {

    SavingAccount createSavingAccountToEntity(CreateSavingAccountRequest createSavingAccountRequest);

    SavingAccountDto toDto(SavingAccount savedAccount);

}
