package org.norma.finalproject.common.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String identity) throws UsernameNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByIdentity(identity);
        if(customer.isEmpty()){
            throw new UsernameNotFoundException("Username not found.");
        }
        log.info("Load by username : {}",customer.get().getName());
        return new UserDetail(customer.get());
    }
}
