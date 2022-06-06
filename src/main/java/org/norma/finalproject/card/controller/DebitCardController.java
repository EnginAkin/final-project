package org.norma.finalproject.card.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.card.service.DebitFacadeService;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("api/v1/cards/debits")
@RequiredArgsConstructor
public class DebitCardController {

    private final DebitFacadeService debitFacadeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid CreateDebitCardRequest createDebitCardRequest) throws BusinessException {
        return debitFacadeService.create(userDetail.getUser().getId(), createDebitCardRequest);
    }

    @GetMapping("/{debitCardID}")
    public GeneralResponse getByID(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long debitCardID) throws BusinessException {
        return debitFacadeService.getDebitCardByID(userDetail.getUser().getId(), debitCardID);
    }

    @PatchMapping("/{debitCardID}")
    public GeneralResponse update(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long debitCardID, @RequestBody UpdateDebitCardRequest updateDebitCardRequest) throws BusinessException {
        return debitFacadeService.update(userDetail.getUser().getId(), debitCardID, updateDebitCardRequest);
    }

    @GetMapping("/{debitCardID}/activities")
    public GeneralResponse getDebitCardActivities(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                                  @PathVariable long debitCardID,
                                                  @RequestBody(required = false) ActivityFilter filter) throws BusinessException {
        return debitFacadeService.getDebitCardActivities(userDetail.getUser().getId(), debitCardID, filter);
    }


    @GetMapping
    public GeneralResponse getAllCustomerDebitCards(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws BusinessException {
        return debitFacadeService.getAllCustomerDebitCards(userDetail.getUser().getId());
    }

    @DeleteMapping("/{debitCardID}")
    public GeneralResponse deleteDebitCardById(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long debitCardID) throws BusinessException {
        return debitFacadeService.deleteDebitCardById(userDetail.getUser().getId(), debitCardID);
    }


}
