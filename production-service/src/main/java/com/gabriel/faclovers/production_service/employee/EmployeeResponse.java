package com.gabriel.faclovers.production_service.employee;

import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        Long companyId,
        String name,
        String role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
