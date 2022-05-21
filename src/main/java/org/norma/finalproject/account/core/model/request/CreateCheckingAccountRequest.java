package org.norma.finalproject.account.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateCheckingAccountRequest {

    @NotNull
    private String accountName;
    @NotNull(message = "Currency type is invalid")
    private CurrencyType currencyType;


}
