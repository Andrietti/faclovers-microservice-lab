package com.gabriel.faclovers.production_service.lot;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot, Long> {

    List<Lot> findByCompanyIdAndActiveTrueOrderByStartDateDesc(Long companyId);

    List<Lot> findByCompanyIdAndPieceModelIdAndActiveTrueOrderByStartDateDesc(Long companyId, Long pieceModelId);

    Optional<Lot> findByIdAndCompanyIdAndActiveTrue(Long id, Long companyId);
}
