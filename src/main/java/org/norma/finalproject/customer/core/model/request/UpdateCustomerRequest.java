package org.norma.finalproject.customer.core.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class UpdateCustomerRequest {

    @NotNull(message = "Password Cannot be null.")
    private String password;

    //@Pattern(regexp = ) telefon num regex
    @NotNull(message = "Telephone Cannot be null.")
    private String telephone;





}
