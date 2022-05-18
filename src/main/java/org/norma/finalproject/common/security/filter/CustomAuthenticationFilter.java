package org.norma.finalproject.common.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.common.security.jwt.JWTHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final JWTHelper jwtHelper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/v1/authentication")){
            filterChain.doFilter(request,response);
        }else{
            String authorizationHeader=request.getHeader(AUTHORIZATION);
            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
              try {
                  String token=authorizationHeader.substring("Bearer ".length());

                  String identity = jwtHelper.findUsername(token);
                  String [] roles=jwtHelper.getCustomerRoles(token);
                  Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
                  stream(roles).forEach(role -> {
                      authorities.add(new SimpleGrantedAuthority(role));
                  });
                  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(identity,null,authorities);
                  SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                  filterChain.doFilter(request,response);
              }catch (Exception e){
                  log.error("CustomAuthenticationFilter error : "+e.getMessage());
                  }
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }
}
