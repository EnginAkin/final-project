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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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

    public String findIdentity(String token) {
        if (!StringUtils.hasLength(token)) {
            throw new IllegalArgumentException("Token can not be null or empty");
        }
        return JWT.decode(token).getSubject();
    }
    public String[] getCustomerRoles(String token) {
        if (!StringUtils.hasLength(token)) {
            throw new IllegalArgumentException("Token can not be null or empty");
        }
        return JWT.decode(token).getClaim("roles").asArray(String.class);
    }


    public boolean validate(String token) {
        try {
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC512(secretKey)).build();
            DecodedJWT decodedJWT= jwtVerifier.verify(token);
            return true;
        } catch (AlgorithmMismatchException algorithmMismatchException) {
            log.error("JWT Token AlgorithmMismatchException occurred!");
            throw new AlgorithmMismatchException("JWT Token invalid algorithm occurred!");
        } catch (SignatureVerificationException signatureVerificationException) {
            log.error("JWT Token invalid signature occurred!");
            throw new SignatureVerificationException(Algorithm.HMAC512(secretKey));
        } catch (TokenExpiredException tokenExpiredException) {
            log.error("JWT Token TokenExpiredException occurred!");
            throw new TokenExpiredException("JWT Token expired");
        } catch (InvalidClaimException invalidClaimException) {
            log.error("JWT Token InvalidClaimException occurred!");
            throw new InvalidClaimException("JWT Token invalid claims");

        }
    }



}
