package org.norma.finalproject.account.entity;


import lombok.*;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.customer.entity.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;


    private BigDecimal balance=BigDecimal.ZERO;

    private BigDecimal lockedBalance=BigDecimal.ZERO;


    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(length = 50)
    private String createdBy;
}
