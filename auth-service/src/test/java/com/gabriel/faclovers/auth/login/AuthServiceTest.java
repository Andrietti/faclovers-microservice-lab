package com.gabriel.faclovers.auth.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.faclovers.auth.company.Company;
import com.gabriel.faclovers.auth.company.CompanyRepository;
import com.gabriel.faclovers.auth.security.JwtProperties;
import com.gabriel.faclovers.auth.security.JwtService;
import com.gabriel.faclovers.auth.shared.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldLoginSuccessfully() {
        var company = company(true);

        when(companyRepository.findByEmail("teste@faclovers.com")).thenReturn(Optional.of(company));
        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);
        when(jwtService.generateToken(company)).thenReturn("jwt-token");
        when(jwtProperties.expirationMinutes()).thenReturn(1440L);

        var response = authService.login(new LoginRequest(" TESTE@FACLOVERS.COM ", "123456"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresInMinutes()).isEqualTo(1440L);
        assertThat(response.companyId()).isEqualTo(1L);
        assertThat(response.companyEmail()).isEqualTo("teste@faclovers.com");
    }

    @Test
    void shouldGenerateTokenOnLogin() {
        var company = company(true);

        when(companyRepository.findByEmail("teste@faclovers.com")).thenReturn(Optional.of(company));
        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);
        when(jwtService.generateToken(company)).thenReturn("jwt-token");
        when(jwtProperties.expirationMinutes()).thenReturn(1440L);

        authService.login(new LoginRequest("teste@faclovers.com", "123456"));

        verify(jwtService).generateToken(company);
    }

    @Test
    void shouldThrowGenericExceptionWhenEmailDoesNotExist() {
        when(companyRepository.findByEmail("teste@faclovers.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest("teste@faclovers.com", "123456")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email ou senha invalidos");

        verify(passwordEncoder, never()).matches("123456", "hash");
    }

    @Test
    void shouldThrowGenericExceptionWhenPasswordIsWrong() {
        var company = company(true);

        when(companyRepository.findByEmail("teste@faclovers.com")).thenReturn(Optional.of(company));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("teste@faclovers.com", "wrong")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email ou senha invalidos");

        verify(jwtService, never()).generateToken(company);
    }

    @Test
    void shouldThrowExceptionWhenCompanyIsInactive() {
        var company = company(false);

        when(companyRepository.findByEmail("teste@faclovers.com")).thenReturn(Optional.of(company));

        assertThatThrownBy(() -> authService.login(new LoginRequest("teste@faclovers.com", "123456")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Empresa inativa");

        verify(passwordEncoder, never()).matches("123456", "hash");
    }

    @Test
    void shouldReturnInvalidWhenAuthorizationHeaderIsMissing() {
        var response = authService.validateToken(null);

        assertThat(response.valid()).isFalse();
        assertThat(response.companyId()).isNull();
    }

    @Test
    void shouldReturnInvalidWhenAuthorizationHeaderIsNotBearer() {
        var response = authService.validateToken("Basic token");

        assertThat(response.valid()).isFalse();
    }

    @Test
    void shouldReturnInvalidWhenTokenIsInvalid() {
        when(jwtService.isTokenValid("invalid-token")).thenReturn(false);

        var response = authService.validateToken("Bearer invalid-token");

        assertThat(response.valid()).isFalse();
    }

    @Test
    void shouldReturnCompanyDataWhenTokenIsValid() {
        when(jwtService.isTokenValid("valid-token")).thenReturn(true);
        when(jwtService.extractCompanyId("valid-token")).thenReturn(1L);
        when(jwtService.extractCompanyName("valid-token")).thenReturn("Facção Teste");
        when(jwtService.extractCompanyEmail("valid-token")).thenReturn("teste@faclovers.com");

        var response = authService.validateToken("Bearer valid-token");

        assertThat(response.valid()).isTrue();
        assertThat(response.companyId()).isEqualTo(1L);
        assertThat(response.companyName()).isEqualTo("Facção Teste");
        assertThat(response.companyEmail()).isEqualTo("teste@faclovers.com");
    }

    private Company company(Boolean active) {
        return Company.builder()
                .id(1L)
                .name("Facção Teste")
                .email("teste@faclovers.com")
                .passwordHash("hash")
                .active(active)
                .build();
    }
}
