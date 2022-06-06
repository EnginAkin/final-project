package org.norma.finalproject.account.core.mapper.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.core.model.response.AccountActivityResponse;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.common.entity.enums.ActionStatus;

import java.math.BigDecimal;
import java.util.Date;


class AccountActivityMapperImplTest {

    private AccountActivityMapperImpl accountActivityMapper = new AccountActivityMapperImpl();

    @Test
    public void givenAccountActivity_whenToDto_thenReturnAccountActivitiyResponse() {
        // given
        AccountActivity accountActivity = new AccountActivity();
        accountActivity.setCrossAccount("111");
        accountActivity.setActionStatus(ActionStatus.INCOMING);
        accountActivity.setAvailableBalance(BigDecimal.TEN);
        accountActivity.setAmount(BigDecimal.TEN);
        accountActivity.setDescription("1111");
        accountActivity.setDate(new Date());
        // when
        AccountActivityResponse accountActivityResponse = accountActivityMapper.toDto(accountActivity);
        // then
        Assertions.assertThat(accountActivityResponse).isNotNull();
        Assertions.assertThat(accountActivityResponse.getAmount()).isEqualTo(BigDecimal.TEN);

    }

}