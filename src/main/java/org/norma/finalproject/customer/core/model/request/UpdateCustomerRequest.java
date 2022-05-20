package org.norma.finalproject.customer.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class UpdateCustomerRequest {

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 6,message = "password size must be 6 digit.")
    private String password;
    private String telephone;


}
