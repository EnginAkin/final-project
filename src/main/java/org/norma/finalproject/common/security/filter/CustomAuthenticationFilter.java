package org.norma.finalproject.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.common.security.jwt.impl.JWTHelper;
import org.norma.finalproject.common.security.user.CustomUserDetail;
import org.norma.finalproject.common.security.user.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final JWTHelper jwtHelper;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/authentication")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    jwtHelper.validate(token);
                    String identity = jwtHelper.findIdentity(token);
                    CustomUserDetail userDetail = (CustomUserDetail) userDetailsService.loadUserByUsername(identity);
                    userDetail.setToken(token);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);

                } catch (Exception e) {
                    Map<String, Object> customResponse = new HashMap<>();
                    customResponse.put("isSuccessfull", false);
                    customResponse.put("message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(403);
                    new ObjectMapper().writeValue(response.getOutputStream(), customResponse);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
