package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.entity.enums.AccountType;
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
        List<AccountActivity> activities = repository.findAllByAccount_Id(accountId);
        return activities;
    }
}
