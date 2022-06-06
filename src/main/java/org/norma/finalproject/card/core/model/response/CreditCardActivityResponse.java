package org.norma.finalproject.card.core.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.norma.finalproject.card.entity.enums.SpendCategory;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class CreditCardActivityResponse {

    private Long id;
    private String crossAccount;
    private BigDecimal amount;
    private String description;
    private SpendCategory spendCategory;
    private Date processDate;

}
