package org.norma.finalproject.account.entity;

import lombok.*;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.customer.entity.Customer;
import javax.persistence.*;

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
    private boolean blocked=false;







}
