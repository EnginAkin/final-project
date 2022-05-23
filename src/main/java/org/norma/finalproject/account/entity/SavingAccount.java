package org.norma.finalproject.account.entity;

import lombok.*;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.enums.Maturity;
import org.norma.finalproject.account.entity.enums.PurposeCumulative;
import org.norma.finalproject.customer.entity.Customer;
import javax.persistence.*;
import java.math.BigDecimal;

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

    @Enumerated(EnumType.STRING)
    private PurposeCumulative purposeCumulative;

    @OneToOne(cascade = CascadeType.ALL)
    private CheckingAccount parentAccount;









}
