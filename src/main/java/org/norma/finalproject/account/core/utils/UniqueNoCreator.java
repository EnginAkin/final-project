package org.norma.finalproject.account.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.norma.finalproject.account.service.BaseAccountService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public final class UniqueNoCreator {
    private final BaseAccountService accountService;


    public String creatAccountNo() {
        String randomDepositAccountNo = RandomStringUtils.randomNumeric(16);
        if (!(accountService.checkIsAccountNoUnique(randomDepositAccountNo))) {
            log.info("Deposit unique account no created : {}", randomDepositAccountNo);
            return randomDepositAccountNo;
        }
        return creatAccountNo();
    }

    public String creatCardNumber() {
        String randomCardNumber = RandomStringUtils.randomNumeric(16);
        if (!(accountService.checkIsAccountNoUnique(randomCardNumber))) {
            log.info("card no created : {}", randomCardNumber);
            return randomCardNumber;
        }
        return creatCardNumber();
    }

    public String createIbanNo(String accountNo, String bankCode) {
        String reservedField = "0";
        String iBANCheckDigits = "33";
        String ibanNumber = "TR" + iBANCheckDigits + bankCode + reservedField + accountNo;
        if (!(accountService.checkIsIbanNoUnique(ibanNumber))) {
            log.info("Deposit unique iban no created : {}", ibanNumber);
            return ibanNumber;
        }
        return creatAccountNo();
    }


}
