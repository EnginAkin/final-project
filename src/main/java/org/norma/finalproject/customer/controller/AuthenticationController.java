package org.norma.finalproject.customer.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.common.security.jwt.impl.JWTHelper;
import org.norma.finalproject.common.security.token.core.exception.TokenNotFoundException;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.LoginFailedException;
import org.norma.finalproject.customer.core.model.request.LoginFormRequest;
import org.norma.finalproject.customer.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/authentication")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    private final JWTHelper jwtHelper;

    @PostMapping("/login")
    public GeneralResponse login(@RequestBody @Valid LoginFormRequest loginFormRequest) throws LoginFailedException {
        return authService.login(loginFormRequest.getUserNumber(), loginFormRequest.getPassword());
    }

    @PostMapping("/logout")
    public GeneralResponse logout(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetail userDetail) throws TokenNotFoundException {
        jwtHelper.deleteTokenForLogout(userDetail.getToken());
        return new GeneralSuccessfullResponse("logout success");
    }


}
