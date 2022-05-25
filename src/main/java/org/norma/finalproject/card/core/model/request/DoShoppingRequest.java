package org.norma.finalproject.card.core.model.request;


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

}
