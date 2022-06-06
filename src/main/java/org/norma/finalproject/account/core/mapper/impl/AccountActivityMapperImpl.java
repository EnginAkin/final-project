package org.norma.finalproject.account.core.mapper.impl;

import org.norma.finalproject.account.core.mapper.AccountActivityMapper;
import org.norma.finalproject.account.core.model.response.AccountActivityResponse;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountActivityMapperImpl implements AccountActivityMapper {
    @Override
    public AccountActivityResponse toDto(AccountActivity accountActivity) {
        AccountActivityResponse response = new AccountActivityResponse();
        response.setCrossAccount(accountActivity.getCrossAccount());
        response.setAmount(accountActivity.getAmount().multiply(BigDecimal.valueOf(accountActivity.getActionStatus().getValue())));
        response.setDescription(accountActivity.getDescription());
        response.setAvailableBalance(accountActivity.getAvailableBalance());
        response.setDate(accountActivity.getDate());
        return response;
    }
}
