package org.norma.finalproject.customer.core.validator.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.customer.core.validator.UniqueEmail;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final CustomerService customerService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<Customer> optionalCustomer = customerService.findCustomerByEmail(email);
        if (optionalCustomer.isPresent()) {
            return false;
        }
        return true;
    }
}
