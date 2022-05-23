package org.norma.finalproject.transfer.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.transfer.core.model.request.CreateTransferRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {


    @PostMapping
    public GeneralResponse transfer(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody CreateTransferRequest createTransferRequest) throws CustomerNotFoundException {
        //return facadeService.transfer(userDetail.getUser().getId(),createTransferRequest);
        return null;
    }

}
