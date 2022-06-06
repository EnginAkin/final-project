package org.norma.finalproject.customer.repository;

import org.norma.finalproject.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdentityNumber(String identity);

    Optional<Customer> findByEmail(String identity);

}
