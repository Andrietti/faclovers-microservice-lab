package com.gabriel.faclovers.production_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of("/swagger-ui", "/v3/api-docs", "/actuator");

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token ausente ou inválido");
            return;
        }

        String token = header.substring(7);
        var companyContext = jwtService.extractCompanyContext(token);

        if (companyContext.isEmpty() || !jwtService.isValid(token)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token ausente ou inválido");
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                companyContext.get(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_COMPANY"))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}
