package org.norma.finalproject.transfer.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.common.entity.BaseModel;
import org.norma.finalproject.transfer.entity.enums.SendType;

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
    @Enumerated(value = EnumType.STRING)
    private SendType sendType;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date processTime;

}
