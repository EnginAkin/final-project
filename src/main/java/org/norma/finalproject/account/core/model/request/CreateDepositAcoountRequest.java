package org.norma.finalproject.account.core.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateDepositAcoountRequest {

    @NotNull
    private String accountName;
    @NotNull(message = "Currency type is invalid")
    private CurrencyType currencyType;


}
