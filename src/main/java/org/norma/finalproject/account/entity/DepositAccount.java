package org.norma.finalproject.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.common.entity.AccountActivity;
import org.norma.finalproject.customer.entity.BaseExtendedModel;
import org.norma.finalproject.customer.entity.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositAccount extends BaseModel {

    private String accountNo;
    private String ibanNo;
    private String accountName;
    private BigDecimal totalBalance=BigDecimal.ZERO;

    @Enumerated(value = EnumType.STRING)
    private CurrencyType currencyType;


    @OneToMany(cascade = CascadeType.ALL)
    private List<AccountActivity> activities=new ArrayList<>();



    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(length = 50)
    private String createdBy;

}
