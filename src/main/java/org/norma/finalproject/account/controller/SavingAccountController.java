package org.norma.finalproject.account.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.service.FacadeSavingAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts/saving")
@RequiredArgsConstructor
public class SavingAccountController {

    private final FacadeSavingAccountService facadeSavingAccountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody CreateSavingAccountRequest createSavingAccountRequest) {
        //return facadeSavingAccountService.create(userDetail.getCustomer().getId(), createSavingAccountRequest);
        return null;

    }


}
