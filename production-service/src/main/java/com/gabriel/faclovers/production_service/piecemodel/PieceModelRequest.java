package com.gabriel.faclovers.production_service.piecemodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PieceModelRequest(@NotBlank @Size(max = 120) String name) {
}
