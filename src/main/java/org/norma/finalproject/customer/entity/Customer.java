package org.norma.finalproject.customer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Customer extends BaseExtendedModel {

    private String identityNumber;
    private String customerNo;
    private String name;
    private String surname;
    private String password;



    @Temporal(TemporalType.DATE)
    private Date birthDay;

    @OneToMany(mappedBy = "customer")
    private Set<Address> addresses=new HashSet<>();


    public void addAddress(Address address){
        addresses.add(address);
    }


}
