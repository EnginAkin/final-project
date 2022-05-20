package org.norma.finalproject.account.entity;

import lombok.*;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.TermPeriod;
import org.norma.finalproject.account.entity.enums.PurposeCumulative;
import org.norma.finalproject.common.entity.BaseModel;
import org.norma.finalproject.customer.entity.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavingAccount extends BaseModel {


    private String accountName;
    private String accountNo;
    private String ibanNo;
    private String accountBalance;
    private BigDecimal successRate = BigDecimal.ZERO;
    private BigDecimal targetAmount = BigDecimal.ZERO;

    @Enumerated(value = EnumType.ORDINAL)
    private TermPeriod termPeriod;
    @Enumerated(EnumType.STRING)
    private PurposeCumulative purposeCumulative;
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "savingAccount")
    List<SavingAccountActivity> activities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    public void addActivities(SavingAccountActivity activity) {
        activities.add(activity);
    }


    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(length = 50)
    private String createdBy;


}
