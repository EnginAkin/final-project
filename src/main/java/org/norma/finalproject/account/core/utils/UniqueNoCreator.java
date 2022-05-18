package org.norma.finalproject.account.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.norma.finalproject.account.service.DepositAccountService;
import org.norma.finalproject.account.service.FacadeDepositAccountService;
import org.springframework.stereotype.Component;

import java.util.Random;

@RequiredArgsConstructor
@Component
@Slf4j
public final class UniqueNoCreator {
    private final DepositAccountService depositAccountService;



    public  String createDepositAccountNo(){
        String randomDepositAccountNo = RandomStringUtils.randomNumeric(12);
        if(depositAccountService.checkIsAccountNoUnique(randomDepositAccountNo)){
            log.info("Deposit unique account no created : {}",randomDepositAccountNo);
            return randomDepositAccountNo;
        }
        return createDepositAccountNo();
    }


}
