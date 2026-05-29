package com.gabriel.faclovers.production_service.lot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LotResponse(
        Long id,
        Long companyId,
        Long pieceModelId,
        String pieceModelName,
        String description,
        BigDecimal pricePerPiece,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalQuantity,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
