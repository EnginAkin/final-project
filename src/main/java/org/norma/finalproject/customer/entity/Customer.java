package org.norma.finalproject.customer.entity;

import lombok.*;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.common.entity.BaseExtendedModel;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer extends BaseExtendedModel {

    private String identityNumber;
    private String customerNo;
    private String name;
    private String surname;
    private String password;
    private String telephone;
    // TODO aylık gelir

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles=new HashSet<>();

    @Temporal(TemporalType.DATE)
    private Date birthDay;



    @OneToMany(mappedBy = "customer")
    private Set<Address> addresses=new HashSet<>();


    public void addAddress(Address address){
        addresses.add(address);
    }


}
