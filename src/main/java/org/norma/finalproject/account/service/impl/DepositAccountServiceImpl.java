package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.repository.DepositAccountRepository;
import org.norma.finalproject.account.service.DepositAccountService;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class DepositAccountServiceImpl implements DepositAccountService {

    private final DepositAccountRepository repository;

    @Override
    public boolean checkIsAccountNoUnique(String accountNo) {
        return !repository.existsDepositAccountByAccountNo(accountNo);
    }
}


