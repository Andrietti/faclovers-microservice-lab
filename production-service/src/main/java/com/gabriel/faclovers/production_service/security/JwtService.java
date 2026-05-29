package com.gabriel.faclovers.production_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean isValid(String token) {
        return extractClaims(token)
                .filter(claims -> claims.getExpiration() == null || claims.getExpiration().after(new Date()))
                .isPresent();
    }

    public Optional<CompanyContext> extractCompanyContext(String token) {
        return extractClaims(token)
                .map(claims -> new CompanyContext(
                        toLong(claims.get("companyId")),
                        claims.get("companyName", String.class),
                        claims.get("companyEmail", String.class)
                ))
                .filter(context -> context.companyId() != null);
    }

    public Optional<Long> extractCompanyId(String token) {
        return extractCompanyContext(token).map(CompanyContext::companyId);
    }

    public Optional<String> extractCompanyName(String token) {
        return extractCompanyContext(token).map(CompanyContext::companyName);
    }

    public Optional<String> extractCompanyEmail(String token) {
        return extractCompanyContext(token).map(CompanyContext::companyEmail);
    }

    public Optional<String> extractSubject(String token) {
        return extractClaims(token).map(Claims::getSubject);
    }

    private Optional<Claims> extractClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (JwtException | IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    private byte[] secretBytes() {
        return jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
    }

    private Long toLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }

        if (value instanceof String text) {
            return Long.valueOf(text);
        }

        return null;
    }
}
