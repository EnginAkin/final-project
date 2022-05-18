package org.norma.finalproject.account.controller;


import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.service.FacadeDepositAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/accounts/deposits")
@RequiredArgsConstructor
@Validated
public class DepositAccountController {

    private final FacadeDepositAccountService depositAccountService;

    @PostMapping
    public GeneralResponse create(@RequestBody @Valid CreateDepositAcoountRequest createDepositAcoountRequest){
        return depositAccountService.create(createDepositAcoountRequest);
    }

}
