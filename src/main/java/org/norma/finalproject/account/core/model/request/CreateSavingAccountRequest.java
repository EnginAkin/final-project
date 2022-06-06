package org.norma.finalproject.account.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.Maturity;
import org.norma.finalproject.account.entity.enums.PurposeSaving;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateSavingAccountRequest {

    @NotNull
    private String parentAccountNumber;

    @NotNull
    private String accountName;

    @NotNull
    private CurrencyType currencyType;

    @NotNull
    private PurposeSaving purposeSaving;

    @NotNull
    @Min(0)
    private BigDecimal targetAmount;

    @NotNull
    private Maturity maturity;
    @NotNull
    @Min(0)
    private BigDecimal openingBalance;
}
