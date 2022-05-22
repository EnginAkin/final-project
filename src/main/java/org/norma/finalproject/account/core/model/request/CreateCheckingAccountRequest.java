package org.norma.finalproject.account.core.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import javax.validation.constraints.*;

@Getter
@Setter
public class CreateCheckingAccountRequest {

    @JsonProperty(defaultValue = "00061") // example bank code
    @Size(max = 6,min = 6)
    private String bankCode;
    @JsonProperty(defaultValue = "FATIH")
    private String branchName;
    @JsonProperty(defaultValue = "50")
    @Min(2)
    private String branchCode;
    @JsonProperty(defaultValue ="TRY")
    private CurrencyType currencyType;


}
