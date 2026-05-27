package com.gabriel.faclovers.auth.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.gabriel.faclovers.auth.company.Company;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRET = "faclovers_microservices_lab_secret_key_with_at_least_32_chars";

    @Test
    void shouldGenerateValidToken() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));

        var token = jwtService.generateToken(company());

        assertThat(token).isNotBlank();
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void shouldExtractCompanyId() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));
        var token = jwtService.generateToken(company());

        assertThat(jwtService.extractCompanyId(token)).isEqualTo(1L);
    }

    @Test
    void shouldExtractCompanyName() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));
        var token = jwtService.generateToken(company());

        assertThat(jwtService.extractCompanyName(token)).isEqualTo("Facção Teste");
    }

    @Test
    void shouldExtractCompanyEmail() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));
        var token = jwtService.generateToken(company());

        assertThat(jwtService.extractCompanyEmail(token)).isEqualTo("teste@faclovers.com");
    }

    @Test
    void shouldExtractSubject() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));
        var token = jwtService.generateToken(company());

        assertThat(jwtService.extractSubject(token)).isEqualTo("teste@faclovers.com");
    }

    @Test
    void shouldIdentifyValidToken() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));
        var token = jwtService.generateToken(company());

        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));

        assertThat(jwtService.isTokenValid("invalid-token")).isFalse();
    }

    @Test
    void shouldReturnFalseForExpiredToken() throws InterruptedException {
        var jwtService = new JwtService(new JwtProperties(SECRET, -1L));
        var token = jwtService.generateToken(company());

        Thread.sleep(5);

        assertThat(jwtService.isTokenValid(token)).isFalse();
    }

    @Test
    void shouldExtractExpiration() {
        var jwtService = new JwtService(new JwtProperties(SECRET, 1440L));
        var token = jwtService.generateToken(company());

        assertThat(jwtService.extractExpiration(token)).isAfter(java.time.Instant.now());
    }

    private Company company() {
        return Company.builder()
                .id(1L)
                .name("Facção Teste")
                .email("teste@faclovers.com")
                .active(true)
                .passwordHash("hash")
                .build();
    }
}
