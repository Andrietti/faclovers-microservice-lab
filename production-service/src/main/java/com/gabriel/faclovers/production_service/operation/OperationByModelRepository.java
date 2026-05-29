package com.gabriel.faclovers.production_service.operation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationByModelRepository extends JpaRepository<OperationByModel, Long> {

    List<OperationByModel> findByCompanyIdAndPieceModelIdAndActiveTrueOrderBySequenceOrder(Long companyId, Long pieceModelId);

    Optional<OperationByModel> findByIdAndCompanyIdAndActiveTrue(Long id, Long companyId);
}
