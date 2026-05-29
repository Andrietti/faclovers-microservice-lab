package com.gabriel.faclovers.production_service.lot;

import com.gabriel.faclovers.production_service.piecemodel.PieceModel;
import org.springframework.stereotype.Component;

@Component
public class LotMapper {

    public Lot toEntity(LotRequest request, PieceModel pieceModel, Long companyId) {
        Lot lot = new Lot();
        lot.setCompanyId(companyId);
        lot.setPieceModel(pieceModel);
        lot.setDescription(request.description());
        lot.setPricePerPiece(request.pricePerPiece());
        lot.setStartDate(request.startDate());
        lot.setEndDate(request.endDate());
        lot.setTotalQuantity(request.totalQuantity());
        lot.setActive(true);
        return lot;
    }

    public LotResponse toResponse(Lot lot) {
        return new LotResponse(
                lot.getId(),
                lot.getCompanyId(),
                lot.getPieceModel().getId(),
                lot.getPieceModel().getName(),
                lot.getDescription(),
                lot.getPricePerPiece(),
                lot.getStartDate(),
                lot.getEndDate(),
                lot.getTotalQuantity(),
                lot.getActive(),
                lot.getCreatedAt(),
                lot.getUpdatedAt()
        );
    }
}
