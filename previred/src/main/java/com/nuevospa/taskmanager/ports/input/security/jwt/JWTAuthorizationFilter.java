package com.nuevospa.taskmanager.ports.input.security.jwt;

import io.jsonwebtoken.*;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private Claims setSigningKey(jakarta.servlet.http.HttpServletRequest request) {
        String jwtToken = request.
                getHeader(Constants.HEADER_AUTHORIZACION_KEY).
                replace(Constants.TOKEN_BEARER_PREFIX, "");

        return Jwts.parserBuilder()
                .setSigningKey(Constants.getSigningKey(Constants.SUPER_SECRET_KEY))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private void setAuthentication(Claims claims) {

        Object authoritiesObject = claims.get("authorities");
        List<String> authorities = null;
        if (authoritiesObject instanceof List<?>) {
            authorities = ((List<?>) authoritiesObject).stream()
                    .filter(item -> item instanceof String)
                    .map(item -> (String) item)
                    .collect(Collectors.toList());
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                        (authorities != null ? authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()) : List.of()));

        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    private boolean isJWTValid(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) {
        String authenticationHeader = request.getHeader(Constants.HEADER_AUTHORIZACION_KEY);
        if (authenticationHeader == null || !authenticationHeader.startsWith(Constants.TOKEN_BEARER_PREFIX))
            return false;
        return true;
    } 
    

    @Override
    protected void doFilterInternal(@NonNull jakarta.servlet.http.HttpServletRequest request,
            @NonNull jakarta.servlet.http.HttpServletResponse response, @NonNull jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
                try {
                    if (isJWTValid(request, response)) {
                        Claims claims = setSigningKey(request);
                        if (claims.get("authorities") != null) {
                            setAuthentication(claims);
                        } else {
                            SecurityContextHolder.clearContext();
                        }
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                    filterChain.doFilter(request, response);
                } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                    return;
                }
    }


}
