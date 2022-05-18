package org.norma.finalproject.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.common.entity.enums.ActionStatus;
import org.norma.finalproject.customer.entity.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class AccountActivity extends BaseModel {
    // todo account ve saving hesapların ortak noktası
    private String crossAccount;
    private BigDecimal amount=BigDecimal.ZERO;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String description;
    private BigDecimal availableBalance=BigDecimal.ZERO;
    @Enumerated(value = EnumType.STRING)
    private ActionStatus actionStatus;



}
