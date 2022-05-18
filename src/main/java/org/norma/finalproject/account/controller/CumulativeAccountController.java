package org.norma.finalproject.account.controller;


import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.model.request.CreateCumulativeAccountRequest;
import org.norma.finalproject.account.service.FacadeCumulativeAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts/cumulative")
@RequiredArgsConstructor
public class CumulativeAccountController {

    private final FacadeCumulativeAccountService facadeCumulativeAccountService;

    @PostMapping("/{customerId}")
    public GeneralResponse create(@PathVariable Long customerId, @RequestBody CreateCumulativeAccountRequest createCumulativeAccountRequest){
        return facadeCumulativeAccountService.create(customerId,createCumulativeAccountRequest);
    }



}
