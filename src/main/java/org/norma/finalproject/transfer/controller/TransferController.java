package org.norma.finalproject.transfer.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.transfer.core.model.request.CreateIbanTransferRequest;
import org.norma.finalproject.transfer.service.TransferAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    @Qualifier("iban")
    private final TransferAdapter transferAdapter;
    @PostMapping
    public GeneralResponse transfer(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody CreateIbanTransferRequest createIbanTransferRequest) throws BusinessException {
        return transferAdapter.transfer(userDetail.getUser().getId(), createIbanTransferRequest);
    }


    // 1. hesap TR3300066100593830134313924  // 50 tl
    // 2. hesap TR3300066109491285046676093 // 10 tl bakiye
}
