package org.norma.finalproject.customer.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.customer.core.model.AddressDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class CreateCustomerRequest {

    @NotNull(message = "Identity number cannot be null.")
    @Size(min = 11,max = 11)
    private String identityNumber;

    @NotNull(message = "name cannot be null.")
    private String name;

    @NotNull(message = "Surname cannot be null.")
    private String surname;

    @NotNull(message = "Password Cannot be null.")
    private String password;

    @Pattern(regexp ="^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$")
    @NotNull(message = "Date Cannot be null.")
    private Date birthDay; // 01/12/2019

    //@Pattern(regexp = ) telefon num regex
    @NotNull(message = "Telephone Cannot be null.")
    private String telephone;

    //private AddressDto address;




}
