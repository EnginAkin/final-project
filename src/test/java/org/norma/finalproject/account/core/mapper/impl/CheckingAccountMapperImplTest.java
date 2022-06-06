package org.norma.finalproject.account.core.mapper.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.enums.CurrencyType;

class CheckingAccountMapperImplTest {

    private CheckingAccountMapperImpl checkingAccountMapper = new CheckingAccountMapperImpl();

    @Test
    public void givenCreateCheckingAccountRequest_whenToEntity_thenReturnEntity() {
        // given
        CreateCheckingAccountRequest request = new CreateCheckingAccountRequest();
        request.setBranchCode("11");
        request.setBankCode("11");
        request.setBranchName("11");
        request.setCurrencyType(CurrencyType.TRY);
        // when
        CheckingAccount checkingAccount = checkingAccountMapper.toEntity(request);
        // then
        Assertions.assertThat(checkingAccount).isNotNull();
        Assertions.assertThat(checkingAccount.getBankCode()).isEqualTo(request.getBankCode());

    }

}