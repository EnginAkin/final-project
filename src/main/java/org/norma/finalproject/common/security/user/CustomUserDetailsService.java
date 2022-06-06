package org.norma.finalproject.common.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.common.entity.User;
import org.norma.finalproject.common.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identity) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserNumber(identity);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username not found.");
        }
        log.info("Load by username : {}", user.get().getUserNumber());
        return new CustomUserDetail(user.get());
    }


}
