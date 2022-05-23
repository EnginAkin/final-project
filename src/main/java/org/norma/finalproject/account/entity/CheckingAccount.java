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
@Setter
@Getter
@PrimaryKeyJoinColumn(name = "id",referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class CheckingAccount extends Account {

    @Column(insertable = false,updatable = false)
    private Long id;

    private String bankCode;
    private String branchCode;
    private String branchName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkingAccount",orphanRemoval = true)
    private List<CheckingAccountActivity> activities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private boolean blocked=false;


}
