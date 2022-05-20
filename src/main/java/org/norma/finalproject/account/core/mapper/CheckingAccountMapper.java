package org.norma.finalproject.account.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.entity.CheckingAccount;

@Mapper(componentModel = "spring")
public interface CheckingAccountMapper {

    CheckingAccountMapper INSTANCE = Mappers.getMapper(CheckingAccountMapper.class);

    CheckingAccount ToEntity(CreateDepositAcoountRequest createDepositAcoountRequest);

    CreateDepositAccountResponse toDto(CheckingAccount checkingAccount);


}
