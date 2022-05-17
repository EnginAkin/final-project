package org.norma.finalproject.customer.service;

import org.norma.finalproject.customer.core.exception.CustomerAlreadyRegisterException;
import org.norma.finalproject.customer.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Customer save(Customer customer) throws CustomerAlreadyRegisterException;
    Optional<Customer> getCustomerByIdentity(String identity);

}
