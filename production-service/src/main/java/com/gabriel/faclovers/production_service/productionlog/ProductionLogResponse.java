package com.gabriel.faclovers.production_service.productionlog;

import java.time.LocalDateTime;

public record ProductionLogResponse(
        Long id,
        Long companyId,
        Long lotId,
        String lotDescription,
        Long employeeId,
        String employeeName,
        Long operationId,
        String operationName,
        LocalDateTime producedAt,
        Integer quantityProduced,
        String notes,
        LocalDateTime createdAt
) {
}
