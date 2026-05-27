package com.gabriel.faclovers.auth.company;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CompanyRequest(
        @NotBlank(message = "nome é obrigatório")
        String name,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "deve ser um endereço de e-mail válido")
        String email,

        @NotBlank(message = "senha é obrigatória")
        @Size(min = 6, message = "senha deve ter pelo menos 6 caracteres")
        String password,

        @DecimalMin(value = "0.00", message = "meta diária deve ser maior ou igual a zero")
        BigDecimal dailyGoalAmount
) {
}
