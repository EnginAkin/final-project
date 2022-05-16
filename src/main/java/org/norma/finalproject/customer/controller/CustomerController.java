package org.norma.finalproject.customer.controller;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.customer.IdentityNotValidException;
import org.norma.finalproject.customer.core.exception.customer.NotAcceptableAgeException;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.service.FacadeCustomerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final FacadeCustomerService facadeCustomerService;

    @PostMapping("sing-up")
    public GeneralResponse create(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, IdentityNotValidException {
        return facadeCustomerService.signup(createCustomerRequest);
    }

}
