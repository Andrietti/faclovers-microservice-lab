package com.gabriel.faclovers.production_service.productionlog;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ProductionLogRequest(
        @NotNull Long lotId,
        @NotNull Long employeeId,
        @NotNull Long operationId,
        LocalDateTime producedAt,
        @NotNull @Positive Integer quantityProduced,
        @Size(max = 255) String notes
) {
}
