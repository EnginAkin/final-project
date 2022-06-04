package org.norma.finalproject.common.atm.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Getter
@Setter
public class DepositRequest {

    private String cardNumber;
    private String cardPassword;
    @Min(0)
    private BigDecimal depositAmount;

}
