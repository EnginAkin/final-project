package org.norma.finalproject.account.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.customer.entity.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class CheckingAccount extends Account {

    @Column(insertable = false, updatable = false)
    private Long id;
    @NotNull
    private String bankCode;
    @NotNull
    private String branchCode;
    @NotNull
    private String branchName;
    private boolean blocked = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
