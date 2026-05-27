package com.gabriel.faclovers.auth.login;

import com.gabriel.faclovers.auth.company.CompanyRepository;
import com.gabriel.faclovers.auth.security.JwtProperties;
import com.gabriel.faclovers.auth.security.JwtService;
import com.gabriel.faclovers.auth.shared.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        var email = request.email().trim().toLowerCase();
        var company = companyRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Email ou senha invalidos", HttpStatus.UNAUTHORIZED));

        if (!Boolean.TRUE.equals(company.getActive())) {
            throw new BusinessException("Empresa inativa", HttpStatus.FORBIDDEN);
        }

        if (!passwordEncoder.matches(request.password(), company.getPasswordHash())) {
            throw new BusinessException("Email ou senha invalidos", HttpStatus.UNAUTHORIZED);
        }

        var token = jwtService.generateToken(company);

        return new LoginResponse(
                token,
                "Bearer",
                jwtProperties.expirationMinutes(),
                company.getId(),
                company.getName(),
                company.getEmail()
        );
    }

    public TokenValidationResponse validateToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return invalidToken();
        }

        var token = authorizationHeader.substring(7).trim();

        if (token.isBlank() || !jwtService.isTokenValid(token)) {
            return invalidToken();
        }

        return new TokenValidationResponse(
                true,
                jwtService.extractCompanyId(token),
                jwtService.extractCompanyName(token),
                jwtService.extractCompanyEmail(token)
        );
    }

    private TokenValidationResponse invalidToken() {
        return new TokenValidationResponse(false, null, null, null);
    }
}
