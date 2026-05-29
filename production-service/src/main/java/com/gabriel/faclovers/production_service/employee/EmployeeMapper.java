package com.gabriel.faclovers.production_service.employee;

import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request, Long companyId) {
        Employee employee = new Employee();
        employee.setCompanyId(companyId);
        employee.setName(request.name());
        employee.setRole(request.role());
        employee.setActive(true);
        return employee;
    }

    public EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getCompanyId(),
                employee.getName(),
                employee.getRole(),
                employee.getActive(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
