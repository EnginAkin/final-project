package org.norma.finalproject.account.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.modelmapper.internal.util.Strings;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public final class UniqueNoCreator {
    private final CheckingAccountService checkingAccountService;


    public String createDepositAccountNo() {
        String randomDepositAccountNo = RandomStringUtils.randomNumeric(16);
        if (checkingAccountService.checkIsAccountNoUnique(randomDepositAccountNo)) {
            log.info("Deposit unique account no created : {}", randomDepositAccountNo);
            return randomDepositAccountNo;
        }
        return createDepositAccountNo();
    }

    public String createDepositIbanNo(String accountNo,String bankCode) {
            String reservedField="0";
            String iBANCheckDigits="33";

            String ibanNumber=CountryCode.TR+iBANCheckDigits+bankCode+reservedField+accountNo;
            // kontrol mekanizması değişecek.
            if (checkingAccountService.checkIsAccountNoUnique(ibanNumber)) {
                log.info("Deposit unique iban no created : {}", ibanNumber);
                return ibanNumber;
            }
            return createDepositAccountNo();

    }



}
