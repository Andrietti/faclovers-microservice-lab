package com.gabriel.faclovers.auth.company;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String email,
        BigDecimal dailyGoalAmount,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
