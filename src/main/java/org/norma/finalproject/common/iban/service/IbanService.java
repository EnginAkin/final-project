package org.norma.finalproject.common.iban.service;

import org.norma.finalproject.customer.entity.Customer;

public interface IbanService {
    void save(String iban, Customer customer);
}
