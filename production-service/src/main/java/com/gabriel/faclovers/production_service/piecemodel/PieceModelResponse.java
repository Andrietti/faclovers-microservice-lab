package com.gabriel.faclovers.production_service.piecemodel;

import java.time.LocalDateTime;

public record PieceModelResponse(
        Long id,
        Long companyId,
        String name,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
