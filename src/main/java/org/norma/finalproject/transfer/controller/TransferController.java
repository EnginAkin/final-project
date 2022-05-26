package org.norma.finalproject.transfer.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.norma.finalproject.common.exception.BusinessException;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.core.model.request.EmailTransferRequest;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/transfers")
public class TransferController {

    private final TransferBase<IbanTransferRequest> transferIban;

    private final TransferBase<EmailTransferRequest> emailTransfer;

    public TransferController( @Qualifier("transfer-iban") TransferBase<IbanTransferRequest> transferIban
                              ,@Qualifier("transfer-email") TransferBase<EmailTransferRequest> emailTransfer) {
        this.transferIban = transferIban;
        this.emailTransfer = emailTransfer;
    }


    @PostMapping("/iban")
    public GeneralResponse transferByIban(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody IbanTransferRequest ibanTransferRequest) throws BusinessException {
        return transferIban.transfer(userDetail.getUser().getId(), ibanTransferRequest);
    }

    @PostMapping("/email")
    public GeneralResponse transferByEmail(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody EmailTransferRequest emailTransferRequest) throws BusinessException {
        return emailTransfer.transfer(userDetail.getUser().getId(), emailTransferRequest);
    }


    // 1. hesap engin.27@gmail.com  // 150 tl
    // 2. hesap engin.28@gmail.com // 100 tl bakiye
}
