package org.norma.finalproject.customer.controller;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;
import org.norma.finalproject.customer.service.FacadeCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final FacadeCustomerService facadeCustomerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/sing-up")
    public GeneralResponse create(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, CustomerAlreadyRegisterException, IdentityNotValidException {
        return facadeCustomerService.signup(createCustomerRequest);
    }

    @PutMapping(path = "/{id}/update")
    public GeneralResponse update(@PathVariable @Min(0) long id, @RequestBody UpdateCustomerRequest updateCustomerRequest) throws  UpdateCustomerSamePasswordException, CustomerNotFoundException {
        return facadeCustomerService.update(id,updateCustomerRequest);
    }

}
