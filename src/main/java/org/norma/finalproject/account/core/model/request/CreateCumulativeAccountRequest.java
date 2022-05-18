package org.norma.finalproject.account.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.PurposeCumulative;
import org.norma.finalproject.account.entity.enums.TermPeriod;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateCumulativeAccountRequest {
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
    private TermPeriod termPeriod;
    @Min(0)
    private BigDecimal openingBalance;
}
