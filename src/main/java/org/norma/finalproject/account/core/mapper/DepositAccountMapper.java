package org.norma.finalproject.account.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.core.model.response.CreateDepositAccountResponse;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.entity.Customer;

@Mapper(componentModel = "spring")
public interface DepositAccountMapper {

    DepositAccountMapper INSTANCE = Mappers.getMapper(DepositAccountMapper.class);

    DepositAccount ToEntity(CreateDepositAcoountRequest createDepositAcoountRequest);

    CreateDepositAccountResponse toDto(DepositAccount depositAccount);


}
