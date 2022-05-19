package org.norma.finalproject.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;
import org.norma.finalproject.customer.service.FacadeCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CustomerController {
    private final FacadeCustomerService facadeCustomerService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = "Customer Controller", description = "Create a new Customer")
    @PostMapping(path = "/sing-up")
    public GeneralResponse create(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, CustomerAlreadyRegisterException, IdentityNotValidException {
        return facadeCustomerService.signup(createCustomerRequest);
    }



    @Operation(tags = "Customer Controller", description = "Update Customer")
    @PutMapping(path = "/{id}/update")
    public GeneralResponse update(@PathVariable @Min(0) long id, @RequestBody UpdateCustomerRequest updateCustomerRequest) throws  UpdateCustomerSamePasswordException, CustomerNotFoundException {
        return facadeCustomerService.update(id,updateCustomerRequest);
    }



    // TODO Kullanıcı  JWT token ile başka  kullanıcıların hesaplarını silebiliyor.
    @Operation(tags = "Customer Controller", description = "Delete customer")
    @PutMapping(path = "/{id}/delete")
    public GeneralResponse delete(@PathVariable @Min(0) long id) throws  CustomerNotFoundException, CustomerDeleteException {
        log.info("gelindi");
            UserDetails customUserDetail =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("gelen id : "+customUserDetail.getUsername());
       // return facadeCustomerService.delete(id);
        return null;
    }

    @Operation(tags = "Customer Controller", description = "Delete customer")
    @PutMapping(path = "/delete")
    public GeneralResponse delete2(@AuthenticationPrincipal CustomUserDetail userDetail) throws  CustomerNotFoundException, CustomerDeleteException {
        log.info("gelindi");
        System.out.println("gelen id : "+userDetail.getUsername());
        // return facadeCustomerService.delete(id);
        return null;
    }



}
