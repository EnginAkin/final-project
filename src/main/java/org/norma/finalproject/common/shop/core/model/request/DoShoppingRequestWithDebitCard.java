package org.norma.finalproject.common.shop.core.model.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class DoShoppingRequestWithDebitCard {

    @NotNull
    private String cardNumber;
    @NotNull
    private String cardPassword;
    @Min(0)
    private BigDecimal shoppingAmount;


}
