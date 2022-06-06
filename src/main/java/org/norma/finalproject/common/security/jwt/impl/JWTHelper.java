package org.norma.finalproject.common.security.jwt.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.common.security.token.core.exception.TokenNotFoundException;
import org.norma.finalproject.common.security.token.entity.JWTToken;
import org.norma.finalproject.common.security.token.service.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Setter
@Getter
@RequiredArgsConstructor
public class JWTHelper {

    private final TokenService tokenService;

    //@Value("${norma.final.project.jwt.secret-key}")
    private String secretKey = "normasecretkey";

    //@Value("${norma.final.project.jwt.expires-in}")
    private long expiresIn = 30000000L;


    public String generate(String identity, List<String> roles) {
        if (!StringUtils.hasLength(identity)) {
            throw new IllegalArgumentException("Identifier no cannot be null");
        }

        String tokenValue = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresIn))
                .withSubject(identity)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC512(secretKey));
        JWTToken JWTTokenObject = new JWTToken();
        JWTTokenObject.setToken(tokenValue);
        JWTTokenObject.setExpiryDate(new Date(System.currentTimeMillis() + expiresIn));
        tokenService.save(JWTTokenObject);
        return tokenValue;
    }

    public String findIdentity(String token) throws TokenNotFoundException {
        if (!StringUtils.hasLength(token)) {
            throw new IllegalArgumentException("Token can not be null or empty");
        }
        JWTToken JWTTokenObject = tokenService.getToken(token);
        return JWT.decode(JWTTokenObject.getToken()).getSubject();
    }

    public void deleteTokenForLogout(String token) throws TokenNotFoundException {
        tokenService.delete(token);
    }

    public void validate(String tokenValue) throws TokenNotFoundException, InvalidClaimException, SignatureVerificationException, AlgorithmMismatchException {
        try {
            JWTToken JWTTokenObject = tokenService.getToken(tokenValue);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
            jwtVerifier.verify(JWTTokenObject.getToken());
        } catch (TokenExpiredException tokenExpiredException) {
            deleteTokenForLogout(tokenValue); // delete token
            log.error("JWT Token TokenExpiredException occurred!");
            throw new TokenExpiredException("JWT Token expired");
        }
    }


}
