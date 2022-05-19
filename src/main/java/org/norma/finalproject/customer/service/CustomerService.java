package org.norma.finalproject.customer.service;

import org.norma.finalproject.customer.core.exception.CustomerAlreadyRegisterException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.core.exception.RoleNotFoundException;
import org.norma.finalproject.customer.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Customer save(Customer customer) throws CustomerAlreadyRegisterException;

    Optional<Customer> getCustomerByIdentity(String identity);

    Optional<Customer> getCustomerById(Long id);

    void update(Customer customer);

    boolean existCustomerById(long id);


    void delete(long id);
}
