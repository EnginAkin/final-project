package org.norma.finalproject.transfer.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.common.entity.BaseModel;
import org.norma.finalproject.transfer.entity.enums.TransferType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class Transfer extends BaseModel {

    private String fromIban;
    private String toIban;
    private BigDecimal balance;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date processTime;

    @Enumerated(value = EnumType.STRING)
    private TransferType transferType;
    @Enumerated(value = EnumType.STRING)
    private CurrencyType currencyType;

}
