package org.norma.finalproject.account.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateDepositAcoountRequest {

    @NotNull
    private String accountName;
    @NotNull
    private CurrencyType currencyType;


}
