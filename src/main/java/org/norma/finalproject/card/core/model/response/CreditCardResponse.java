package org.norma.finalproject.card.core.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class CreditCardResponse {

    private Long id;
    private String cardNumber;
    private String password;
    private BigDecimal creditLimit;
    private Date expiryDate;
    private Date cutOffDate;
    private Date paymentDate;

}
