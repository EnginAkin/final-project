package org.norma.finalproject.account.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/accounts/checking")
@RequiredArgsConstructor
@Validated
public class CheckingAccountController {

    private final FacadeCheckinAccountService facadeCheckinAccountService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = "Deposit Controller", description = "Create a deposit account By customer.")
    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid CreateCheckingAccountRequest createCheckingAccountRequest) throws BusinessException  {
        return facadeCheckinAccountService.create(userDetail.getUser().getId(), createCheckingAccountRequest);
    }

    @Operation(tags = "Deposit Controller", description = "Delete a deposit account by Customer.")
    @DeleteMapping("/{accountName}")
    public GeneralResponse deleteByAccountName(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,@PathVariable String accountName) throws BusinessException {
        return facadeCheckinAccountService.delete(userDetail.getUser().getId(),accountName);

    }
    @GetMapping
    public GeneralResponse getAllCheckingAccounts(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws BusinessException {
        return facadeCheckinAccountService.getAccounts(userDetail.getUser().getId());
    }


    @RolesAllowed({CustomerConstant.ROLE_ADMIN})
    @Operation(tags = "Deposit Controller", description = "Blocked a deposit account by ADMIN.")
    @PutMapping("/{accountId}/block")
    public GeneralResponse blockedAccount(@PathVariable long accountId) throws BusinessException {
         return facadeCheckinAccountService.blockAccount(accountId);

    }



}
