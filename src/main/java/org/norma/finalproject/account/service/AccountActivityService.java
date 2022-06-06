package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.base.AccountActivity;

import java.util.List;

public interface AccountActivityService {
    List<AccountActivity> getAccountActivitiesByAccountId(long accountId);
}
