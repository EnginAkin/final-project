package org.norma.finalproject.customer.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.customer.entity.enums.AddressType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CustomerAddressRequest {

    @NotNull
    private AddressType addressType;
    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String district;
    @NotNull
    private String streetNumber;


}
