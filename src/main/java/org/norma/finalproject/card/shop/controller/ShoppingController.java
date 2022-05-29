package org.norma.finalproject.card.shop.controller;


import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.core.model.request.DoShoppingRequest;
import org.norma.finalproject.card.shop.service.ShoppingService;
import org.norma.finalproject.common.core.exception.BusinessException;
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
    public void doShopping(@RequestBody DoShoppingRequest doShoppingRequest)throws BusinessException {
        shoppingService.shoppingWithDebitCard(doShoppingRequest);
    }

}
