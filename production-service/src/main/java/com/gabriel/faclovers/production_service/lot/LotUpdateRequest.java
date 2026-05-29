package com.gabriel.faclovers.production_service.lot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LotUpdateRequest(
        @NotNull Long pieceModelId,
        @NotBlank @Size(max = 160) String description,
        @NotNull @PositiveOrZero BigDecimal pricePerPiece,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull @Positive Integer totalQuantity
) {
}
