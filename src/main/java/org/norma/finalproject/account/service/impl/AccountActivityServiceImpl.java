package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.repository.AccountActiviyRepository;
import org.norma.finalproject.account.service.AccountActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountActivityServiceImpl implements AccountActivityService {

    private final AccountActiviyRepository repository;

    @Override
    public List<AccountActivity> getAccountActivityByAccountNumber(String accountNo) {
        return repository.findAllByAccount_AccountNo(accountNo);
    }
}
