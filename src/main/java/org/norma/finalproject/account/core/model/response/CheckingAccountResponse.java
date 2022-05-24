package org.norma.finalproject.account.core.model.response;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import java.math.BigDecimal;

@Getter
@Setter
public class CheckingAccountResponse {

    private long id;
    private String accountName;
    private String accountNo;
    private String ibanNo;
    private CurrencyType currencyType;
    private BigDecimal balance;
}
