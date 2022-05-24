package org.norma.finalproject.card.core.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateDebitCardRequest {

    private String password;
    private BigDecimal dailyLimit;
}
