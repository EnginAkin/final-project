package org.norma.finalproject.card.core.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class DoShoppingRequest {

    @NotNull
    private String CardNumber;
    @NotNull
    private String cardPassword;
    @Min(0)
    private BigDecimal shoppingAmount;

    @JsonProperty(defaultValue = "TR00..")
    private String ToIbanNumber;// this is a scenario

}
