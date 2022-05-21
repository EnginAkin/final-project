package org.norma.finalproject.customer.entity;

import lombok.*;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.common.entity.BaseExtendedModel;
import org.norma.finalproject.common.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "id",referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends User {

    @Column(insertable = false,updatable = false)
    private Long id;

    private String identityNumber;
    private String customerNo;
    private String name;
    private String surname;
    private String password;
    private String telephone;
    private BigDecimal income;



    @Temporal(TemporalType.DATE)
    private Date birthDay;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<CheckingAccount> checkingAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<SavingAccount> savingAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private Set<Address> addresses = new HashSet<>();
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Column(length = 50)
    private String createdBy;
    @Column(length = 50)
    private String updatedBy;
    @Column(length = 50)
    private String deletedBy;

    private boolean isDeleted;

    public void addAddress(Address address) {
        addresses.add(address);
    }


}
