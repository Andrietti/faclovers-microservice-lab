package com.gabriel.faclovers.auth.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email é obrigatório")
        @Email(message = "deve ser um endereço de e-mail válido")
        String email,

        @NotBlank(message = "senha é obrigatória")
        String password
) {
}
