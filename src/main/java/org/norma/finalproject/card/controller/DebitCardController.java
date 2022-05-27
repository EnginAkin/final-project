package org.norma.finalproject.card.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.card.service.DebitFacadeService;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;


@RestController
@RequestMapping("api/v1/cards/debits")
@RequiredArgsConstructor
public class DebitCardController {

    private final DebitFacadeService debitFacadeService;

    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,@RequestBody @Valid CreateDebitCardRequest createDebitCardRequest) throws BusinessException {
        return debitFacadeService.create(userDetail.getUser().getId(),createDebitCardRequest);
    }

    @GetMapping("/{debitCardID}")
    public GeneralResponse getOne(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long debitCardID) throws BusinessException {
        return debitFacadeService.getByID(userDetail.getUser().getId(),debitCardID);
    }
    @PatchMapping("/{debitCardID}")
    public GeneralResponse update(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,@PathVariable long debitCardID,@RequestBody UpdateDebitCardRequest updateDebitCardRequest) throws BusinessException {
        return debitFacadeService.update(userDetail.getUser().getId(),debitCardID,updateDebitCardRequest);
    }

    @GetMapping
    public GeneralResponse getAllCustomerDebitCards(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws BusinessException {
        return debitFacadeService.getAllCustomerDebitCards(userDetail.getUser().getId());
    }

    @DeleteMapping("/{debitCardID}")
    public GeneralResponse deleteDebitCardById(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,@PathVariable long debitCardID) throws BusinessException {
        return debitFacadeService.deleteDebitCardById(userDetail.getUser().getId(),debitCardID);
    }


    @GetMapping("/{debitCardID}/activities")
    public GeneralResponse getDebitCardActivities(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,
                                                  @PathVariable long debitCardID,
                                                  @RequestBody(required = false) ActivityFilter filter
    ) throws BusinessException {
        return debitFacadeService.getDebitCardActivities(userDetail.getUser().getId(),debitCardID,filter);
    }







}
