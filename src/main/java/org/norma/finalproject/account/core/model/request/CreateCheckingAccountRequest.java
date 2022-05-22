package org.norma.finalproject.account.core.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateCheckingAccountRequest {


    @NotNull
    @JsonProperty(defaultValue = "00061") // example bank code
    private String bankCode;

    @NotNull
    private String branchName;
    @NotNull
    private String branchCode;
    @NotNull
    private CurrencyType currencyType;


}
