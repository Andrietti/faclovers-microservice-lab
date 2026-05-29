package com.gabriel.faclovers.production_service.employee;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByCompanyIdAndActiveTrueOrderByName(Long companyId);

    Optional<Employee> findByIdAndCompanyId(Long id, Long companyId);

    Optional<Employee> findByIdAndCompanyIdAndActiveTrue(Long id, Long companyId);
}
