package com.gabriel.faclovers.auth.login;

public record LoginResponse(
        String token,
        String tokenType,
        Long expiresInMinutes,
        Long companyId,
        String companyName,
        String companyEmail
) {
}
