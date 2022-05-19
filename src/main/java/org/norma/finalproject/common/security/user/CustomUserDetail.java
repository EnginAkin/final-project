package org.norma.finalproject.common.security.user;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public record CustomUserDetail(Customer customer) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customer.getRoles().stream().map(this::createSimpleGrantedAuthorities).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getIdentityNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    private SimpleGrantedAuthority createSimpleGrantedAuthorities(Role role) {
        return new SimpleGrantedAuthority(role.getName());
    }
}

