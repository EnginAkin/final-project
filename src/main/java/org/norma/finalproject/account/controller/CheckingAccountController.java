package org.norma.finalproject.account.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/accounts/checking")
@RequiredArgsConstructor
@Validated
public class CheckingAccountController {
    private final FacadeCheckinAccountService facadeCheckinAccountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                  @RequestBody @Valid CreateCheckingAccountRequest createCheckingAccountRequest) throws BusinessException {
        return facadeCheckinAccountService.create(userDetail.getUser().getId(), createCheckingAccountRequest);
    }


    @GetMapping
    public GeneralResponse getAllCustomersCheckingAccounts(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws BusinessException {
        return facadeCheckinAccountService.getCustomersUnblockedCheckingAccounts(userDetail.getUser().getId());
    }

    @GetMapping("/{accountID}")
    public GeneralResponse getById(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                   @PathVariable @Min(0) long accountID) throws BusinessException {
        return facadeCheckinAccountService.getCheckingAccountById(userDetail.getUser().getId(), accountID);
    }

    @GetMapping(value = "/{accountID}/activities")
    public GeneralResponse getCheckingAccountActivities(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                                        @PathVariable @Min(0) long accountID,
                                                        @RequestBody(required = false) ActivityFilter filter) throws BusinessException {
        return facadeCheckinAccountService.getCheckingAccountActivities(userDetail.getUser().getId(), accountID, filter);
    }

    @RolesAllowed(CustomerConstant.ROLE_ADMIN)
    @PatchMapping("/{accountId}/block")
    public GeneralResponse blockedAccount(@PathVariable @Min(0) long accountId) throws BusinessException {
        return facadeCheckinAccountService.blockAccount(accountId);

    }

    @DeleteMapping("/{accountID}")
    public GeneralResponse deleteByAccountId(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                             @PathVariable @Min(0) long accountID) throws BusinessException {
        return facadeCheckinAccountService.deleteById(userDetail.getUser().getId(), accountID);

    }

}

