package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.jwt.JWTHelper;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.customer.core.exception.LoginFailedException;
import org.norma.finalproject.customer.core.model.response.LoginResponse;
import org.norma.finalproject.customer.core.utilities.Utils;
import org.norma.finalproject.customer.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTHelper jwtHelper;

    @Override
    public GeneralResponse login(String identity, String password) throws LoginFailedException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identity, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

            List<String> roles = Utils.SimpleGrantedAuthorityToListString((Collection<GrantedAuthority>) customUserDetail.getAuthorities());
            String token = jwtHelper.generate(identity, roles);
            LoginResponse loginResponse = new LoginResponse(token);
            return GeneralDataResponse.builder().data(loginResponse).build();
        }catch (Exception e){
            throw new LoginFailedException();
        }
    }
}
