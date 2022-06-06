package org.norma.finalproject.customer.service;

import org.norma.finalproject.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer save(Customer customer);

    Optional<Customer> findCustomerByIdentity(String identity);

    Optional<Customer> findByCustomerById(Long id);

    Optional<Customer> findCustomerByEmail(String email);

    void update(Customer customer);

    void delete(Customer customer);

    List<Customer> getall();

}
