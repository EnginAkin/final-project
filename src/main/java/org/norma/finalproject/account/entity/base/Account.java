package org.norma.finalproject.account.entity.base;


import lombok.*;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.card.core.model.request.ActivityFilter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private String ibanNo;
    @NotNull
    private String accountNo;
    @NotNull
    private String accountName;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal lockedBalance = BigDecimal.ZERO;


    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    private List<AccountActivity> activities = new ArrayList<>();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(length = 50)
    private String createdBy;

    public void addActivity(AccountActivity accountActivity) {
        activities.add(accountActivity);
    }

    public List<AccountActivity> getActivityWithFilterDate(ActivityFilter filter) {
        return activities.stream().filter(accountActivity -> accountActivity.getDate().before(filter.getToDate()) &&
                accountActivity.getDate().after(filter.getFromDate())).toList();
    }


}
