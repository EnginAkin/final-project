package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.account.service.FacadeDepositAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeDepositAccountServiceImpl implements FacadeDepositAccountService {


    @Override
    public GeneralResponse create(CreateDepositAcoountRequest createDepositAcoountRequest) {
        DepositAccount depositAccount= DepositAccount.builder().accountName(createDepositAcoountRequest.getAccountName()).currencyType(createDepositAcoountRequest.getCurrencyType()).build();


        return null;
    }
}
