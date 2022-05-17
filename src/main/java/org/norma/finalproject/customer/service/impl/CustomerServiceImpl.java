package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.customer.core.exception.CustomerAlreadyRegisterException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) throws CustomerAlreadyRegisterException {
        Optional<Customer> optionalCustomer = getCustomerByIdentity(customer.getIdentityNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyRegisterException();
        }
        customer.setCreatedBy("ENGIN AKIN");
        customer.setCreatedAt(new Date());
        log.info("Customer successfull register  :{}",customer.getName());
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomerByIdentity(String identity) {
        Optional<Customer> customer=customerRepository.findByIdentityNumber(identity);
        return customer;
    }


}
