package org.norma.finalproject.customer.core.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginFormRequest {
    @NotNull(message = "Identity or customer number cannot be null")
    private String identity;
    @NotNull(message = "Password cannot be null")
    private String password;

}
