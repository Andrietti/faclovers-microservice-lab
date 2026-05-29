package com.gabriel.faclovers.production_service.productionlog;

import com.gabriel.faclovers.production_service.employee.Employee;
import com.gabriel.faclovers.production_service.employee.EmployeeRepository;
import com.gabriel.faclovers.production_service.lot.Lot;
import com.gabriel.faclovers.production_service.lot.LotRepository;
import com.gabriel.faclovers.production_service.operation.OperationByModel;
import com.gabriel.faclovers.production_service.operation.OperationByModelRepository;
import com.gabriel.faclovers.production_service.security.CurrentCompany;
import com.gabriel.faclovers.production_service.shared.BusinessException;
import com.gabriel.faclovers.production_service.shared.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductionLogService {

    private final ProductionLogRepository productionLogRepository;
    private final LotRepository lotRepository;
    private final EmployeeRepository employeeRepository;
    private final OperationByModelRepository operationRepository;
    private final ProductionLogMapper productionLogMapper;
    private final CurrentCompany currentCompany;

    @Transactional
    public ProductionLogResponse create(ProductionLogRequest request) {
        Long companyId = currentCompany.id();
        Lot lot = findLot(request.lotId(), companyId);
        Employee employee = findEmployee(request.employeeId(), companyId);
        OperationByModel operation = findOperation(request.operationId(), companyId);

        if (!operation.getPieceModel().getId().equals(lot.getPieceModel().getId())) {
            throw new BusinessException("Operação não pertence ao modelo de peça do lote");
        }

        EmployeeProductionLog log = productionLogMapper.toEntity(request, lot, employee, operation, companyId);
        return productionLogMapper.toResponse(productionLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public List<ProductionLogResponse> findAll() {
        return productionLogRepository.findByCompanyIdOrderByProducedAtDesc(currentCompany.id())
                .stream()
                .map(productionLogMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductionLogResponse findById(Long id) {
        return productionLogMapper.toResponse(productionLogRepository.findByIdAndCompanyId(id, currentCompany.id())
                .orElseThrow(() -> new NotFoundException("Registro de produção não encontrado")));
    }

    @Transactional(readOnly = true)
    public List<ProductionLogResponse> findByLot(Long lotId) {
        Long companyId = currentCompany.id();
        findLot(lotId, companyId);
        return productionLogRepository.findByCompanyIdAndLotIdOrderByProducedAtDesc(companyId, lotId)
                .stream()
                .map(productionLogMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductionLogResponse> findByEmployee(Long employeeId) {
        Long companyId = currentCompany.id();
        findEmployee(employeeId, companyId);
        return productionLogRepository.findByCompanyIdAndEmployeeIdOrderByProducedAtDesc(companyId, employeeId)
                .stream()
                .map(productionLogMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductionLogResponse> findByDate(LocalDate date) {
        return productionLogRepository.findByCompanyIdAndProducedAtBetweenOrderByProducedAtDesc(
                        currentCompany.id(),
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay().minusNanos(1)
                )
                .stream()
                .map(productionLogMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        EmployeeProductionLog log = productionLogRepository.findByIdAndCompanyId(id, currentCompany.id())
                .orElseThrow(() -> new NotFoundException("Registro de produção não encontrado"));
        productionLogRepository.delete(log);
    }

    private Lot findLot(Long lotId, Long companyId) {
        return lotRepository.findByIdAndCompanyIdAndActiveTrue(lotId, companyId)
                .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
    }

    private Employee findEmployee(Long employeeId, Long companyId) {
        return employeeRepository.findByIdAndCompanyIdAndActiveTrue(employeeId, companyId)
                .orElseThrow(() -> new NotFoundException("Funcionária não encontrada"));
    }

    private OperationByModel findOperation(Long operationId, Long companyId) {
        return operationRepository.findByIdAndCompanyIdAndActiveTrue(operationId, companyId)
                .orElseThrow(() -> new NotFoundException("Operação não encontrada"));
    }
}
