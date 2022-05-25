package org.norma.finalproject.card.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitOperationException;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.DoShoppingRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.service.DebitFacadeService;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/v1/cards/debits")
@RequiredArgsConstructor
public class DebitCardController {

    private final DebitFacadeService debitFacadeService;

    @PostMapping
    public GeneralResponse create(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,@RequestBody @Valid CreateDebitCardRequest createDebitCardRequest) throws BusinessException {
        return debitFacadeService.create(userDetail.getUser().getId(),createDebitCardRequest);
    }

    @GetMapping("/{debitID}")
    public GeneralResponse getOne(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable long debitID) throws BusinessException {
        return debitFacadeService.getByID(userDetail.getUser().getId(),debitID);
    }
    @PatchMapping("/{debitCardID}")
    public GeneralResponse update(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,@PathVariable long debitCardID,@RequestBody UpdateDebitCardRequest updateDebitCardRequest) throws BusinessException {
        return debitFacadeService.update(userDetail.getUser().getId(),debitCardID,updateDebitCardRequest);
    }
    @GetMapping
    public GeneralResponse getAllDebitCards(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws CustomerNotFoundException, DebitOperationException {
        return debitFacadeService.getAllCustomersDebitCards(userDetail.getUser().getId());
    }

    @PostMapping("/shoppings")
    public GeneralResponse shoppingWithDebitCard(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail,@RequestBody DoShoppingRequest doShoppingRequest) throws BusinessException {
        return debitFacadeService.shoppingWithCard(userDetail.getUser().getId(),doShoppingRequest);
    }







}
