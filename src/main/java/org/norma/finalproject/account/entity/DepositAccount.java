package org.norma.finalproject.account.entity;

import lombok.*;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.common.entity.BaseModel;
import org.norma.finalproject.customer.entity.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DepositAccount extends BaseModel {

    private String accountNo;
    private String ibanNo;
    private String accountName;
    private BigDecimal totalBalance=BigDecimal.ZERO;

    @Enumerated(value = EnumType.STRING)
    private CurrencyType currencyType;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "depositAccount")
    private List<DepositAccountActivity> activities=new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;




    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(length = 50)
    private String createdBy;

}