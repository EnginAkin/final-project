package org.norma.finalproject.card.core.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DebitCardResponse {

    private Long id;
    private String cardNumber;
    private String password;
    private String cvv;
    private BigDecimal balance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal dailyLimit;
    private Date expiryDate;
}
