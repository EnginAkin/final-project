package org.norma.finalproject.account.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public final class UniqueNoCreator {
    private final CheckingAccountService checkingAccountService;


    public String createDepositAccountNo() {
        String randomDepositAccountNo = RandomStringUtils.randomNumeric(12);
        if (checkingAccountService.checkIsAccountNoUnique(randomDepositAccountNo)) {
            log.info("Deposit unique account no created : {}", randomDepositAccountNo);
            return randomDepositAccountNo;
        }
        return createDepositAccountNo();
    }

    public String createDepositIbanNo() {
        String randomDepositAccountNo = RandomStringUtils.randomNumeric(16);
        if (checkingAccountService.checkIsAccountNoUnique(randomDepositAccountNo)) {
            log.info("Deposit unique iban no created : {}", randomDepositAccountNo);
            return toFormatIban(randomDepositAccountNo);
        }
        return createDepositAccountNo();
    }

    private String toFormatIban(String value) {
        String formattedIban = "";
        for (int i = 0; i < 4; i++) {
            formattedIban += value.substring(i * 4, i * 4 + 4) + " ";
        }

        return formattedIban.substring(0, formattedIban.length() - 1); // remove last space index
    }


}
