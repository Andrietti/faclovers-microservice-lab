package com.gabriel.faclovers.production_service.operation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OperationByModelRequest(
        @NotNull Long pieceModelId,
        @NotBlank @Size(max = 120) String name,
        @Size(max = 255) String description,
        @NotNull @Positive Integer durationMinutes,
        @NotNull @Positive Integer sequenceOrder
) {
}
