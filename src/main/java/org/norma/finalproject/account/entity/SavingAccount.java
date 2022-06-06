package org.norma.finalproject.account.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.enums.Maturity;
import org.norma.finalproject.account.entity.enums.PurposeSaving;
import org.norma.finalproject.customer.entity.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class SavingAccount extends Account {
    @Column(insertable = false, updatable = false)
    private Long id;

    private BigDecimal successRate = BigDecimal.ZERO;
    private BigDecimal targetAmount = BigDecimal.ZERO;

    @Enumerated(value = EnumType.ORDINAL)
    private Maturity maturity;

    @Temporal(TemporalType.DATE)
    private Date maturityDate; // Maturity date -> for giving interest .

    @Enumerated(EnumType.STRING)
    private PurposeSaving purposeSaving;

    @OneToOne
    private CheckingAccount parentAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
