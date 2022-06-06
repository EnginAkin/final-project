package org.norma.finalproject.common.repository;

import org.norma.finalproject.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNumber(String identity);
}
