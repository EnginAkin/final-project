package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.repository.AccountRepository;
import org.norma.finalproject.account.service.BaseAccountService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseAccountServiceImpl implements BaseAccountService {
    private final AccountRepository repository;


    @Override
    public boolean checkIsAccountNoUnique(String accountNo) {
        return repository.existsAccountByAccountNo(accountNo);
    }

    @Override
    public boolean checkIsIbanNoUnique(String ibanNo) {
        return repository.existsAccountByIbanNo(ibanNo);
    }

    @Override
    public Account update(Account account) {
        return repository.save(account);
    }

    @Override
    public Optional<Account> findAccountByIbanNumberAndCustomerId(String iban,long customerId) {
        if(StringUtils.isEmpty(iban)){
            throw new IllegalArgumentException("Iban cannot bu null");
        }
       // return repository.findByIbanNoAndCustomer_Id(iban,customerId);
        return null;
    }

    @Override
    public Optional<Account> findAccountByIbanNumber(String iban) {
        if(StringUtils.isEmpty(iban)){
            throw new IllegalArgumentException("Iban cannot bu null");
        }
        return repository.findByIbanNo(iban);
    }

    @Override
    public Optional<Account> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Account> findAccountByEmail(String email) {
        //return repository.findByCustomer_Email(email);
        return null;
    }

}
