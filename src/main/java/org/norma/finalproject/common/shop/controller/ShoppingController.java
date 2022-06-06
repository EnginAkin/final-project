package org.norma.finalproject.common.shop.controller;


import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.core.exception.BusinessException;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.shop.core.model.request.DoShoppingRequestWithCreditCard;
import org.norma.finalproject.common.shop.core.model.request.DoShoppingRequestWithDebitCard;
import org.norma.finalproject.common.shop.service.ShoppingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/shopping")
@RequiredArgsConstructor
public class ShoppingController {

    private final ShoppingService shoppingService;

    @PostMapping("/debit-card")
    public GeneralResponse doShoppingWithDebitCard(@RequestBody DoShoppingRequestWithDebitCard doShoppingRequestWithDebitCard) throws BusinessException {
        return shoppingService.shoppingWithDebitCard(doShoppingRequestWithDebitCard);
    }

    @PostMapping("/credit-card")
    public GeneralResponse doShoppingWithCreditCard(@RequestBody DoShoppingRequestWithCreditCard shoppingRequest) throws BusinessException {
        return shoppingService.shoppingWithCreditCard(shoppingRequest);
    }
}
