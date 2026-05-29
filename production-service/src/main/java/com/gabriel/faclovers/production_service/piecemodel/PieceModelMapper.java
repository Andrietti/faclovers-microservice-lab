package com.gabriel.faclovers.production_service.piecemodel;

import org.springframework.stereotype.Component;

@Component
public class PieceModelMapper {

    public PieceModel toEntity(PieceModelRequest request, Long companyId) {
        PieceModel pieceModel = new PieceModel();
        pieceModel.setCompanyId(companyId);
        pieceModel.setName(request.name());
        pieceModel.setActive(true);
        return pieceModel;
    }

    public PieceModelResponse toResponse(PieceModel pieceModel) {
        return new PieceModelResponse(
                pieceModel.getId(),
                pieceModel.getCompanyId(),
                pieceModel.getName(),
                pieceModel.getActive(),
                pieceModel.getCreatedAt(),
                pieceModel.getUpdatedAt()
        );
    }
}
