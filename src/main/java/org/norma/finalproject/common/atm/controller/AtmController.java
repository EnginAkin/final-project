package org.norma.finalproject.common.atm.controller;


import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.atm.model.request.DepositRequest;
import org.norma.finalproject.common.atm.model.request.WithdrawRequest;
import org.norma.finalproject.common.atm.service.AtmService;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/atm")
@RequiredArgsConstructor
public class AtmController {

    private final AtmService atmService;

    @PatchMapping("/deposit")
    public GeneralResponse depositToCardInATM(@RequestBody @Valid DepositRequest depositRequest) throws BusinessException {
        return atmService.depositToCard(depositRequest);
    }

    @PatchMapping("/withdraws")
    public GeneralResponse withdrawFromCardInATM(@RequestBody @Valid WithdrawRequest withdrawRequest) throws BusinessException {
        return atmService.withdrawFromCard(withdrawRequest);
    }


}
