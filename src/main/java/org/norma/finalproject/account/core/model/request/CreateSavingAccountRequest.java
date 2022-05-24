package org.norma.finalproject.account.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.PurposeCumulative;
import org.norma.finalproject.account.entity.enums.Maturity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateSavingAccountRequest {

    @NotNull
    private String parentAccountNumber; // id ile değiştir

    @NotNull
    private String accountName;

    @NotNull
    private CurrencyType currencyType;

    @NotNull
    private PurposeCumulative purposeCumulative;

    @NotNull
    @Min(0)
    private BigDecimal targetAmount;

    @NotNull
    private Maturity maturity;
    @NotNull
    @Min(0)
    private BigDecimal openingBalance;
}
