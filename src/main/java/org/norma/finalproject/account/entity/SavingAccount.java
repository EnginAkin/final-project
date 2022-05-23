package org.norma.finalproject.account.entity;

import lombok.*;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.Maturity;
import org.norma.finalproject.account.entity.enums.PurposeCumulative;
import org.norma.finalproject.common.entity.BaseModel;
import org.norma.finalproject.customer.entity.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@PrimaryKeyJoinColumn(name = "id",referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class SavingAccount extends Account {

    @Column(insertable = false,updatable = false)
    private Long id;

    private String accountName;
    private BigDecimal balance;
    private BigDecimal successRate = BigDecimal.ZERO;
    private BigDecimal targetAmount = BigDecimal.ZERO;

    @Enumerated(value = EnumType.ORDINAL)
    private Maturity maturity;
    @Enumerated(EnumType.STRING)
    private PurposeCumulative purposeCumulative;
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;



    @OneToOne(cascade = CascadeType.ALL)
    private CheckingAccount parentAccount;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "savingAccount")
    List<SavingAccountActivity> activities = new ArrayList<>();
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
