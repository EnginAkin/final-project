package org.norma.finalproject.account.core.mapper;

import org.norma.finalproject.account.core.model.response.AccountActivityResponse;
import org.norma.finalproject.account.entity.base.AccountActivity;

public interface AccountActivityMapper {

    AccountActivityResponse toDto(AccountActivity accountActivity);
}
