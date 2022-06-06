package org.norma.finalproject.common.security.config;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.security.filter.CustomAuthenticationFilter;
import org.norma.finalproject.common.security.user.CustomUserDetailsService;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST_FOR_SWAGGER = {
            // swagger 3 open api
            "/v3/api-docs/**",
            "/swagger-ui/**",
    };
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/customers/sing-up/**",
            "/api/v1/authentication/login/**",
            "/api/v1/shopping/**",
            "/api/v1/atm/**",
    };
    private static final String[] AUTH_WHITELIST_FOR_USER = {
            "/api/v1/customers/**",
            "/api/v1/accounts/**"
    };
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationFilter authenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers(AUTH_WHITELIST_FOR_SWAGGER).permitAll();
        http.authorizeHttpRequests().antMatchers(AUTH_WHITELIST).permitAll();
        http.authorizeHttpRequests().antMatchers(AUTH_WHITELIST_FOR_USER).hasAnyAuthority(CustomerConstant.ROLE_USER);
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }


    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
