package com.gabriel.faclovers.production_service.productionlog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionLogRepository extends JpaRepository<EmployeeProductionLog, Long> {

    List<EmployeeProductionLog> findByCompanyIdOrderByProducedAtDesc(Long companyId);

    Optional<EmployeeProductionLog> findByIdAndCompanyId(Long id, Long companyId);

    List<EmployeeProductionLog> findByCompanyIdAndLotIdOrderByProducedAtDesc(Long companyId, Long lotId);

    List<EmployeeProductionLog> findByCompanyIdAndEmployeeIdOrderByProducedAtDesc(Long companyId, Long employeeId);

    List<EmployeeProductionLog> findByCompanyIdAndProducedAtBetweenOrderByProducedAtDesc(Long companyId, LocalDateTime start, LocalDateTime end);
}
