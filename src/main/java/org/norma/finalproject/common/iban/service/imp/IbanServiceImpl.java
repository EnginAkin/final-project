package org.norma.finalproject.common.iban.service.imp;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.iban.entity.Iban;
import org.norma.finalproject.common.iban.repository.IbanRepository;
import org.norma.finalproject.common.iban.service.IbanService;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IbanServiceImpl implements IbanService {

    private final IbanRepository ibanRepository;

    @Override
    public void save(String ibanNumber, Customer customer) {
        Iban iban =new Iban();
        iban.setIbanNumber(ibanNumber);
        iban.setCustomer(customer);
        ibanRepository.save(iban);
    }
}
