package org.norma.finalproject.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.common.entity.enums.ActionStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class AccountActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String crossAccount;
    private BigDecimal amount;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String description;
    private BigDecimal availableBalance;
    @Enumerated(value = EnumType.STRING)
    private ActionStatus actionStatus;


}
