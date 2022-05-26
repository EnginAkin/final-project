package org.norma.finalproject.card.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.core.model.request.CreateCreditCardRequest;
import org.norma.finalproject.card.service.CreditCardFacadeService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cards/credits")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardFacadeService creditCardFacadeService;

    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody CreateCreditCardRequest createCreditCardRequest) throws CustomerNotFoundException {
        return creditCardFacadeService.create(userDetail.getUser().getId(),createCreditCardRequest);
    }


}
