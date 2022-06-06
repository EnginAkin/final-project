package org.norma.finalproject.card.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.core.model.request.CreateCreditCardRequest;
import org.norma.finalproject.card.service.CreditCardFacadeService;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/cards/credits")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardFacadeService creditCardFacadeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid CreateCreditCardRequest createCreditCardRequest) throws BusinessException {
        return creditCardFacadeService.create(userDetail.getUser().getId(), createCreditCardRequest);
    }

    @GetMapping("/{creditCardId}/current-term-transactions")
    public GeneralResponse getCreditCardActivities(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable("creditCardId") long creditCardId) throws BusinessException {
        return creditCardFacadeService.getCurrentTermTransactions(userDetail.getUser().getId(), creditCardId);
    }

    @GetMapping
    public GeneralResponse getCustomerCreditCards(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws BusinessException {
        return creditCardFacadeService.getCustomerCreditCards(userDetail.getUser().getId());
    }

    @GetMapping("/{creditCardId}/debt")
    public GeneralResponse getCreditCardDebt(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable("creditCardId") long creditCardId) throws BusinessException {
        return creditCardFacadeService.getCreditCardDebt(userDetail.getUser().getId(), creditCardId);
    }

    @DeleteMapping("/{creditCardId}")
    public GeneralResponse deleteCreditCard(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long creditCardId) throws BusinessException {
        return creditCardFacadeService.deleteCreditCard(userDetail.getUser().getId(), creditCardId);
    }
}