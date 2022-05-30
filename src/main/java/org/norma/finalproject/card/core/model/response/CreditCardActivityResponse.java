package org.norma.finalproject.card.core.model.response;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.card.entity.enums.SpendCategory;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreditCardActivityResponse {

    private Long id;
    private String crossAccount;
    private BigDecimal amount;
    private String description;
    private SpendCategory spendCategory;
    private Date processDate;

}
