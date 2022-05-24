package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.repository.AccountActivityRepository;
import org.norma.finalproject.account.service.AccountActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountActivityServiceImpl implements AccountActivityService {
    private final AccountActivityRepository repository;

    @Override
    public List<AccountActivity> getAccountActivitiesByAccountId(long accountId) {
        return repository.findAllByAccount_Id(accountId);
    }
}
