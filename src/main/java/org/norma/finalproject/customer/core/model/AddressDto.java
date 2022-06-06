package org.norma.finalproject.customer.core.model;

import lombok.*;
import org.norma.finalproject.customer.entity.enums.AddressType;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDto {

    private AddressType addressType;
    private String country;
    private String city;
    private String state;
    private String district;
    private String streetNumber;
}
