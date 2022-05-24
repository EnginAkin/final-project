package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.entity.enums.AccountType;

import java.util.List;

public interface AccountActivityService {
    List<AccountActivity> getAccountActivitiesByAccountIdAndCustomerID(long accountNo, long customerId, AccountType accountType);
}
