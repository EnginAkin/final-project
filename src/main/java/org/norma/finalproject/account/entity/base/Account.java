package org.norma.finalproject.account.entity.base;


import lombok.*;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.entity.enums.CurrencyType;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ibanNo;
    private String accountNo;
    private String accountName;
    private BigDecimal balance=BigDecimal.ZERO;
    private BigDecimal lockedBalance=BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "account")
    private List<AccountActivity> activities=new ArrayList<>();

    public void addActivity(AccountActivity accountActivity){
        activities.add(accountActivity);
    }



    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(length = 50)
    private String createdBy;
}
