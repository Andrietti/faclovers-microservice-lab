package com.gabriel.faclovers.production_service.productionlog;

import com.gabriel.faclovers.production_service.employee.Employee;
import com.gabriel.faclovers.production_service.lot.Lot;
import com.gabriel.faclovers.production_service.operation.OperationByModel;
import org.springframework.stereotype.Component;

@Component
public class ProductionLogMapper {

    public EmployeeProductionLog toEntity(ProductionLogRequest request, Lot lot, Employee employee, OperationByModel operation, Long companyId) {
        EmployeeProductionLog log = new EmployeeProductionLog();
        log.setCompanyId(companyId);
        log.setLot(lot);
        log.setEmployee(employee);
        log.setOperation(operation);
        log.setProducedAt(request.producedAt());
        log.setQuantityProduced(request.quantityProduced());
        log.setNotes(request.notes());
        return log;
    }

    public ProductionLogResponse toResponse(EmployeeProductionLog log) {
        return new ProductionLogResponse(
                log.getId(),
                log.getCompanyId(),
                log.getLot().getId(),
                log.getLot().getDescription(),
                log.getEmployee().getId(),
                log.getEmployee().getName(),
                log.getOperation().getId(),
                log.getOperation().getName(),
                log.getProducedAt(),
                log.getQuantityProduced(),
                log.getNotes(),
                log.getCreatedAt()
        );
    }
}
