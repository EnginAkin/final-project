package org.norma.finalproject.common.security.token.repository;

import org.norma.finalproject.common.security.token.entity.JWTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<JWTToken, Long> {

    Optional<JWTToken> findByToken(String token);
}
