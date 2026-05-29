package com.gabriel.faclovers.production_service.piecemodel;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PieceModelRepository extends JpaRepository<PieceModel, Long> {

    List<PieceModel> findByCompanyIdAndActiveTrueOrderByName(Long companyId);

    Optional<PieceModel> findByIdAndCompanyIdAndActiveTrue(Long id, Long companyId);
}
