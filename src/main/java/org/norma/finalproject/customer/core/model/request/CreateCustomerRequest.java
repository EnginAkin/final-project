package org.norma.finalproject.customer.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.customer.core.model.AddressDto;
import org.norma.finalproject.customer.core.model.CustomerInfoDto;

import javax.validation.Valid;

@Getter
@Setter
public class CreateCustomerRequest {

    @Valid
    private CustomerInfoDto customerInfo;

    @Valid
    private AddressDto address;
    @Valid
    private CreateCheckingAccountRequest checkingAccount;

}
