package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.repository.AccountRepository;
import org.norma.finalproject.account.service.BaseAccountService;
import org.springframework.stereotype.Service;

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
}
