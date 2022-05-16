package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
