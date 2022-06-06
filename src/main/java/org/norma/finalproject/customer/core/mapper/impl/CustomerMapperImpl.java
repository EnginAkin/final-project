package org.norma.finalproject.customer.core.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.customer.core.mapper.CustomerMapper;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.entity.Address;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomerMapperImpl implements CustomerMapper {
    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer customerInfoDtoToCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = Customer.builder().name(createCustomerRequest.getCustomerInfo().getName())
                .surname(createCustomerRequest.getCustomerInfo().getSurname()).
                birthDay(createCustomerRequest.getCustomerInfo().getBirthDay())
                .email(createCustomerRequest.getCustomerInfo().getEmail())
                .income(createCustomerRequest.getCustomerInfo().getIncome())
                .telephone(createCustomerRequest.getCustomerInfo().getTelephone())
                .identityNumber(createCustomerRequest.getCustomerInfo().getIdentityNumber())
                .createdAt(new Date())
                .createdBy("ENGIN AKIN")
                .build();
        customer.setPassword(passwordEncoder.encode(createCustomerRequest.getCustomerInfo().getPassword()));
        customer.setUserNumber(customer.getIdentityNumber());

        Address address = new Address();
        address.setCustomer(customer);
        address.setCity(createCustomerRequest.getAddress().getCity());
        address.setCountry(address.getCountry());
        address.setDistrict(createCustomerRequest.getAddress().getDistrict());
        address.setStreetNumber(createCustomerRequest.getAddress().getStreetNumber());
        address.setState(createCustomerRequest.getAddress().getState());
        customer.setAddresses(Set.of(address));

        return customer;
    }
}
