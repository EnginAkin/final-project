package org.norma.finalproject.account.core.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateCheckingAccountRequest {

    @JsonProperty(defaultValue = "000601") // example bank code
    @Size(max = 6, min = 6, message = "Bank code must be 6 digit.")
    private String bankCode;
    @JsonProperty(defaultValue = "FATIH")
    private String branchName;
    @JsonProperty(defaultValue = "50")
    @Size(max = 2, min = 2, message = "Branch code must be 2 digit.")
    private String branchCode;
    @JsonProperty(defaultValue = "TRY")
    private CurrencyType currencyType;


}
