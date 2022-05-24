package org.norma.finalproject.account.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid CreateCheckingAccountRequest createCheckingAccountRequest) throws BusinessException {
        return facadeCheckinAccountService.create(userDetail.getUser().getId(), createCheckingAccountRequest);
    }

    @Operation(tags = "Deposit Controller", description = "Delete a deposit account by Customer.")
    @DeleteMapping("/{accountID}")
    public GeneralResponse deleteByAccountName(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long accountID) throws BusinessException {
        return facadeCheckinAccountService.deleteById(userDetail.getUser().getId(), accountID);

    }

    @GetMapping
    public GeneralResponse getAllCheckingAccounts(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws BusinessException {
        return facadeCheckinAccountService.getCheckingAccounts(userDetail.getUser().getId());
    }
    @GetMapping("{accountID}")
    public GeneralResponse getById(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,long accountID) throws BusinessException {
        return facadeCheckinAccountService.getCheckingAccountById(userDetail.getUser().getId(),accountID);
    }

    @GetMapping("/{accountID}/activities")
    public GeneralResponse getCheckingAccountActivities(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long accountID) throws BusinessException {
        return facadeCheckinAccountService.getCheckingAccountActivities(userDetail.getUser().getId(), accountID);
    }

    @Operation(tags = "Deposit Controller", description = "Blocked a deposit account by ADMIN.")
    @PutMapping("/{accountId}/block")
    public GeneralResponse blockedAccount(@PathVariable long accountId) throws BusinessException {
        return facadeCheckinAccountService.blockAccount(accountId);

    }


}
