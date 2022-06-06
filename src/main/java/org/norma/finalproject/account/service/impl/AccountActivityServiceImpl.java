package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.repository.AccountActivityRepository;
import org.norma.finalproject.account.service.AccountActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountActivityServiceImpl implements AccountActivityService {
    private final AccountActivityRepository repository;

    @Override
    public List<AccountActivity> getAccountActivitiesByAccountId(long accountId) {
        log.debug("get account activities by account id function started.");
        List<AccountActivity> activities = repository.findAllByAccount_Id(accountId);
        log.debug("get account activities by account id function ended.");
        return activities;
    }
}
