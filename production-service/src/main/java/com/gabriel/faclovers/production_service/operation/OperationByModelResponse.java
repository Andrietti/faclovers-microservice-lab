package com.gabriel.faclovers.production_service.operation;

import java.time.LocalDateTime;

public record OperationByModelResponse(
        Long id,
        Long companyId,
        Long pieceModelId,
        String pieceModelName,
        String name,
        String description,
        Integer durationMinutes,
        Integer sequenceOrder,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
