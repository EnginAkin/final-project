package org.norma.finalproject.customer.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer customerDtoToCustomer(CreateCustomerRequest createCustomerRequest);

}
