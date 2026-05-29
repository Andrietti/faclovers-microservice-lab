package com.gabriel.faclovers.production_service.security;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRET = "faclovers_microservices_lab_secret_key_with_at_least_32_chars";
    private final JwtService jwtService = new JwtService(new JwtProperties(SECRET));

    @Test
    void shouldValidateToken() {
        String token = token();

        assertThat(jwtService.isValid(token)).isTrue();
    }

    @Test
    void shouldExtractCompanyId() {
        String token = token();

        assertThat(jwtService.extractCompanyId(token)).contains(1L);
    }

    @Test
    void shouldReturnInvalidForInvalidToken() {
        assertThat(jwtService.isValid("invalid-token")).isFalse();
    }

    private String token() {
        return Jwts.builder()
                .subject("empresa@faclovers.com")
                .claim("companyId", 1L)
                .claim("companyName", "FacLovers")
                .claim("companyEmail", "empresa@faclovers.com")
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
