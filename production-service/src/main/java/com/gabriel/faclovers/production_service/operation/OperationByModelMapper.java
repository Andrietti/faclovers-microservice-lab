package com.gabriel.faclovers.production_service.operation;

import com.gabriel.faclovers.production_service.piecemodel.PieceModel;
import org.springframework.stereotype.Component;

@Component
public class OperationByModelMapper {

    public OperationByModel toEntity(OperationByModelRequest request, PieceModel pieceModel, Long companyId) {
        OperationByModel operation = new OperationByModel();
        operation.setCompanyId(companyId);
        operation.setPieceModel(pieceModel);
        operation.setName(request.name());
        operation.setDescription(request.description());
        operation.setDurationMinutes(request.durationMinutes());
        operation.setSequenceOrder(request.sequenceOrder());
        operation.setActive(true);
        return operation;
    }

    public OperationByModelResponse toResponse(OperationByModel operation) {
        return new OperationByModelResponse(
                operation.getId(),
                operation.getCompanyId(),
                operation.getPieceModel().getId(),
                operation.getPieceModel().getName(),
                operation.getName(),
                operation.getDescription(),
                operation.getDurationMinutes(),
                operation.getSequenceOrder(),
                operation.getActive(),
                operation.getCreatedAt(),
                operation.getUpdatedAt()
        );
    }
}
