package org.norma.finalproject.customer.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.common.entity.BaseModel;
import org.norma.finalproject.customer.entity.enums.AddressType;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Address extends BaseModel {

    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    private String country;
    private String city;
    private String state;
    private String district;
    private String streetNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;


}
