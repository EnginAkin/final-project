package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.customer.core.exception.CustomerAlreadyRegisterException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.core.exception.RoleNotFoundException;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.customer.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;
    private final RoleService roleService;

    @Override
    public Customer save(Customer customer) throws CustomerAlreadyRegisterException {
        Optional<Customer> optionalCustomer = getCustomerByIdentity(customer.getIdentityNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyRegisterException();
        }
        customer.setCreatedBy("ENGIN AKIN");
        customer.setCreatedAt(new Date());
        Role roleUser=roleService.getRoleByName(CustomerConstant.ROLE_USER);
        customer.setRoles(Set.of(roleUser));
        log.info("Customer successfull register  :{}",customer.getName());
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomerByIdentity(String identity) {
        return customerRepository.findByIdentityNumber(identity);
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void update(Customer customer) {
         customerRepository.save(customer);
    }

    @Override
    public boolean existCustomerById(long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public void delete(long id) {
        customerRepository.deleteById(id);
    }


}
