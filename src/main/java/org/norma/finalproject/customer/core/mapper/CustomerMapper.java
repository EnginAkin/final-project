package org.norma.finalproject.customer.core.mapper;

import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.entity.Customer;

public interface CustomerMapper {


    Customer customerInfoDtoToCustomer(CreateCustomerRequest createCustomerRequest);

}
