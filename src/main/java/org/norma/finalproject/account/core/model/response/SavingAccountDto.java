package org.norma.finalproject.account.core.model.response;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.PurposeSaving;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SavingAccountDto {

    private long id;
    private String iban;
    private String accountNo;
    private CurrencyType currencyType;
    private BigDecimal balance;
    private String parentAccountNumber;
    private PurposeSaving purposeSaving;
    private Date maturityDate;
    private BigDecimal successRate;
    private BigDecimal targetAmount;


}
