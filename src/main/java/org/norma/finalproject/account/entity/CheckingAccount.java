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
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@PrimaryKeyJoinColumn(name = "id",referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class CheckingAccount extends Account {


    @Column(insertable = false,updatable = false)
    private Long id;

    private String accountName;
    private String bankCode;
    private String branchCode;
    private String branchName;
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(value = EnumType.STRING)
    private CurrencyType currencyType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkingAccount",orphanRemoval = true)
    private List<CheckingAccountActivity> activities = new ArrayList<>();



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;



    private boolean blocked=false;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(length = 50)
    private String createdBy;

}
