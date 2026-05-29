package com.gabriel.faclovers.production_service.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 80) String role
) {
}
