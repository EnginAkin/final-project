package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.customer.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;
    private final RoleService roleService;

    @Override
    public Customer save(Customer customer) {
        Role roleUser = roleService.getRoleByName(CustomerConstant.ROLE_USER);
        customer.setRoles(Set.of(roleUser));
        log.info("Customer successfull register  :{}", customer.getName());
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findCustomerByIdentity(String identity) {
        return customerRepository.findByIdentityNumber(identity);
    }

    @Override
    public Optional<Customer> findByCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void update(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getall() {
        return customerRepository.findAll();
    }


}
