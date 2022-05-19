package org.norma.finalproject.account.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.service.FacadeDepositAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/accounts/deposits")
@RequiredArgsConstructor
@Validated
public class DepositAccountController {

    private final FacadeDepositAccountService facadeDepositAccountService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = "Deposit Controller", description = "Create a deposit account.")
    @PostMapping("/{customerId}")
    public GeneralResponse create(@PathVariable Long customerId, @RequestBody @Valid CreateDepositAcoountRequest createDepositAcoountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        return facadeDepositAccountService.create(customerId, createDepositAcoountRequest);
    }

}
