package org.norma.finalproject.account.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.service.FacadeSavingAccountService;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/accounts/saving")
@RequiredArgsConstructor
public class SavingAccountController {

    private final FacadeSavingAccountService facadeSavingAccountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                  @RequestBody CreateSavingAccountRequest createSavingAccountRequest) throws BusinessException {
        return facadeSavingAccountService.create(userDetail.getUser().getId(), createSavingAccountRequest);

    }

    @GetMapping("/{accountID}")
    public GeneralResponse getSavingAccountByID(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                                @PathVariable @Min(0) long accountID) throws BusinessException {
        return facadeSavingAccountService.getAccountByAccountID(userDetail.getUser().getId(), accountID);
    }

    @GetMapping
    public GeneralResponse getCustomersAllSavingAccounts(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws BusinessException {
        return facadeSavingAccountService.getAccounts(userDetail.getUser().getId());
    }

    @GetMapping("{accountID}/activities")
    public GeneralResponse getAccountActivity(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                              @PathVariable @Min(0) long accountID,
                                              @RequestBody(required = false) ActivityFilter filter) throws BusinessException {
        return facadeSavingAccountService.getAccountActivities(userDetail.getUser().getId(), accountID, filter);
    }

    @DeleteMapping("/{accountID}")
    public GeneralResponse deleteSavingAccount(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                               @PathVariable @Min(0) long accountID) throws BusinessException {
        return facadeSavingAccountService.deleteById(userDetail.getUser().getId(), accountID);
    }

}
