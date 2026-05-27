package com.gabriel.faclovers.auth.login;

public record TokenValidationResponse(
        Boolean valid,
        Long companyId,
        String companyName,
        String companyEmail
) {
}
