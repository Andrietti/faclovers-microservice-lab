package com.gabriel.faclovers.auth.company;

import com.gabriel.faclovers.auth.shared.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CompanyResponse create(CompanyRequest request) {
        var email = request.email().trim().toLowerCase();

        if (companyRepository.existsByEmail(email)) {
            throw new BusinessException("Email já cadastrado", HttpStatus.CONFLICT);
        }

        var company = Company.builder()
                .name(request.name().trim())
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .dailyGoalAmount(request.dailyGoalAmount())
                .active(true)
                .build();

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public CompanyResponse findById(Long id) {
        return companyRepository.findById(id)
                .map(CompanyMapper::toResponse)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada", HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email.trim().toLowerCase());
    }
}
