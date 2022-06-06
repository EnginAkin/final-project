package org.norma.finalproject.card.core.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreditCardResponse {

    private Long id;
    private String cardNumber;
    private String customerName;
    private String customerSurname;
    private String cvv;
    private String password;
    private BigDecimal creditLimit;
    private BigDecimal availableBalance;
    private Date expiryDate;
    private Date cutOffDate;
    private Date paymentDate;

}
