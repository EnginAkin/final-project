package org.norma.finalproject.customer.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.common.security.jwt.JWTHelper;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.LoginFailedException;
import org.norma.finalproject.customer.core.model.request.LoginFormRequest;
import org.norma.finalproject.customer.core.model.response.LoginResponse;
import org.norma.finalproject.customer.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/authentications")
@Validated
@RequiredArgsConstructor
public class AuthanticationController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public GeneralResponse login(@RequestBody @Valid LoginFormRequest loginFormRequest) throws LoginFailedException {
        return authService.login(loginFormRequest.getIdentity(), loginFormRequest.getPassword());
    }

    @PostMapping("/logout")
    public GeneralResponse logout() throws LoginFailedException {

        return new GeneralSuccessfullResponse("logout success");
    }


}
