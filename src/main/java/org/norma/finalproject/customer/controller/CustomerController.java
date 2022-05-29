package org.norma.finalproject.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;
import org.norma.finalproject.customer.service.FacadeCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CustomerController {
    private final FacadeCustomerService facadeCustomerService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = "Customer Controller", description = "Create a new Customer")
    @PostMapping(path = "/sing-up")
    public GeneralResponse create(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, CustomerAlreadyRegisterException, IdentityNotValidException, CustomerNotFoundException, AccountNameAlreadyHaveException {
        return facadeCustomerService.signup(createCustomerRequest);
    }

    @Operation(tags = "Customer Controller", description = "Update Customer By Customer")
    @PutMapping(path = "/update")
    public GeneralResponse update(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody UpdateCustomerRequest updateCustomerRequest) throws UpdateCustomerSamePasswordException, CustomerNotFoundException {
        return facadeCustomerService.update(userDetail.getUser().getId(),updateCustomerRequest);
    }


    @Operation(tags = "Customer Controller", description = "Delete customer By Customer")
    @DeleteMapping(path = "/delete")
    public GeneralResponse delete(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws CustomerNotFoundException, CustomerDeleteException {
        return facadeCustomerService.delete(userDetail.getUser().getId());

    }

    @Operation(tags = "Customer Controller", description = "Delete customer By Customer")
    @GetMapping(path = "/getall")
    public String getall() throws CustomerNotFoundException, CustomerDeleteException {
        //return facadeCustomerService.getall();
        return "ok.";

    }





}
