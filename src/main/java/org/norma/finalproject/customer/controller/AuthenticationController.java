package org.norma.finalproject.customer.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.common.security.jwt.JWTHelper;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.LoginFailedException;
import org.norma.finalproject.customer.core.model.request.LoginFormRequest;
import org.norma.finalproject.customer.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/authentications")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/login")
    public GeneralResponse login(@RequestBody @Valid LoginFormRequest loginFormRequest) throws LoginFailedException {
        return authService.login(loginFormRequest.getIdentity(), loginFormRequest.getPassword());
    }

    @PostMapping("/logout")
    public GeneralResponse logout(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) {
        JWTHelper.addJWTBlackList(userDetail.getToken());
        return new GeneralSuccessfullResponse("logout success");
    }


}
