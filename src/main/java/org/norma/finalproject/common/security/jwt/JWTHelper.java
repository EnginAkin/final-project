package org.norma.finalproject.common.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JWTHelper {


    @Value("${norma.final.project.jwt.secret-key}")
    private String secretKey;
    @Value("${norma.final.project.jwt.expires-in}")
    private long expiresIn;

    public String generate(String identity, List<String> roles) {
        if (!StringUtils.hasLength(identity)) {
            throw new IllegalArgumentException("Identifier no cannot be null");
        }
        return JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+expiresIn))
                .withSubject(identity)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String findUsername(String token) {
        if (!StringUtils.hasLength(token)) {
            throw new IllegalArgumentException("Token can not be null or empty");
        }
        return JWT.decode(token).getClaim("identity").asString();
    }
    public String[] getCustomerRoles(String token) {
        if (!StringUtils.hasLength(token)) {
            throw new IllegalArgumentException("Token can not be null or empty");
        }
        return JWT.decode(token).getClaim("roles").asArray(String.class);
    }


    boolean validate(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);
            return true;
        } catch (AlgorithmMismatchException algorithmMismatchException) {
            log.error("JWT Token AlgorithmMismatchException occurred!");
        } catch (SignatureVerificationException signatureVerificationException) {
            log.error("JWT Token SignatureVerificationException occurred!");
        } catch (TokenExpiredException tokenExpiredException) {
            log.error("JWT Token TokenExpiredException occurred!");
        } catch (InvalidClaimException invalidClaimException) {
            log.error("JWT Token InvalidClaimException occurred!");
        }
        return false;
    }



}
