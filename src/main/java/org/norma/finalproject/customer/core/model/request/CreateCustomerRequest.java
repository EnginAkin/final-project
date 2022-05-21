package org.norma.finalproject.customer.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.customer.core.model.AddressDto;
import org.norma.finalproject.customer.core.model.CustomerInfoDto;

@Getter
@Setter
public class CreateCustomerRequest {

    private CustomerInfoDto customerInfo;

    private AddressDto address;

    private CreateCheckingAccountRequest checkingAccount;

}
