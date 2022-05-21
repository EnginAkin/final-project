package org.norma.finalproject.customer.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.norma.finalproject.customer.core.model.AddressDto;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.entity.Address;
import org.norma.finalproject.customer.entity.Customer;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);


    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);

}
