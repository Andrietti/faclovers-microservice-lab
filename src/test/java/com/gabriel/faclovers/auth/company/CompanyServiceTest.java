package com.gabriel.faclovers.auth.company;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.faclovers.auth.shared.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void shouldCreateCompanySuccessfully() {
        var request = new CompanyRequest(" Facção Teste ", " TESTE@FACLOVERS.COM ", "123456", BigDecimal.valueOf(1000));
        var savedCompany = company(1L, "Facção Teste", "teste@faclovers.com", "hash", true);

        when(companyRepository.existsByEmail("teste@faclovers.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hash");
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        var response = companyService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Facção Teste");
        assertThat(response.email()).isEqualTo("teste@faclovers.com");
        assertThat(response.dailyGoalAmount()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(response.active()).isTrue();
    }

    @Test
    void shouldEncryptPasswordWhenCreatingCompany() {
        var request = new CompanyRequest("Facção Teste", "teste@faclovers.com", "123456", null);
        var captor = ArgumentCaptor.forClass(Company.class);

        when(companyRepository.existsByEmail("teste@faclovers.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encrypted-password");
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

        companyService.create(request);

        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("encrypted-password");
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        var request = new CompanyRequest("Facção Teste", "teste@faclovers.com", "123456", null);

        when(companyRepository.existsByEmail("teste@faclovers.com")).thenReturn(true);

        assertThatThrownBy(() -> companyService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email");

        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void shouldFindCompanyByIdSuccessfully() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company(1L, "Facção Teste", "teste@faclovers.com", "hash", true)));

        var response = companyService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("teste@faclovers.com");
    }

    @Test
    void shouldThrowExceptionWhenCompanyDoesNotExist() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.findById(1L))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void shouldReturnTrueWhenCompanyExistsById() {
        when(companyRepository.existsById(1L)).thenReturn(true);

        assertThat(companyService.existsById(1L)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCompanyDoesNotExistById() {
        when(companyRepository.existsById(1L)).thenReturn(false);

        assertThat(companyService.existsById(1L)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        when(companyRepository.existsByEmail("teste@faclovers.com")).thenReturn(true);

        assertThat(companyService.existsByEmail(" TESTE@FACLOVERS.COM ")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        when(companyRepository.existsByEmail("teste@faclovers.com")).thenReturn(false);

        assertThat(companyService.existsByEmail("teste@faclovers.com")).isFalse();
    }

    private Company company(Long id, String name, String email, String passwordHash, Boolean active) {
        return Company.builder()
                .id(id)
                .name(name)
                .email(email)
                .passwordHash(passwordHash)
                .dailyGoalAmount(BigDecimal.valueOf(1000))
                .active(active)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
