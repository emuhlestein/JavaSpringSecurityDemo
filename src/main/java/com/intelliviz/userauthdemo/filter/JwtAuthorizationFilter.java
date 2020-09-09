package com.intelliviz.userauthdemo.filter;

import com.intelliviz.userauthdemo.constant.SecurityConstant;
import com.intelliviz.userauthdemo.utility.JsonWebTokenProvider;
import org.apache.catalina.security.SecurityClassLoad;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private JsonWebTokenProvider tokenProvider;

    public JwtAuthorizationFilter(JsonWebTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
            String username = tokenProvider.getSubject(token);
            if(tokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
                Authentication authentication = tokenProvider.getAuthentication(username, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication); // user is authenticated
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
