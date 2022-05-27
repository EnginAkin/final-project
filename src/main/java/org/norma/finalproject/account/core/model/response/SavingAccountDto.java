package org.norma.finalproject.account.core.model.response;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.PurposeCumulative;

import java.math.BigDecimal;

@Getter
@Setter
public class SavingAccountDto {

    private long id;
    private String iban;
    private String accountNo;
    private CurrencyType currencyType;
    private BigDecimal balance;
    private String parentAccountNumber;
    private PurposeCumulative purposeCumulative;
    private BigDecimal successRate;
    private BigDecimal targetAmount;

}
