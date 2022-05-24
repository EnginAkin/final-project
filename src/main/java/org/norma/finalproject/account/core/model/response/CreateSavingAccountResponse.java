package org.norma.finalproject.account.core.model.response;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSavingAccountResponse {

    private long id;
    private String iban;
    private String accountName;
    private String accountNo;
    private CurrencyType currencyType;
    private BigDecimal balance;
    private String parentAccountNumber;


}
