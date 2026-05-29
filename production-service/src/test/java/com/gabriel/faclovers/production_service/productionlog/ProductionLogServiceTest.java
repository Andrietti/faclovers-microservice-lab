package com.gabriel.faclovers.production_service.productionlog;

import static com.gabriel.faclovers.production_service.TestFixtures.employee;
import static com.gabriel.faclovers.production_service.TestFixtures.lot;
import static com.gabriel.faclovers.production_service.TestFixtures.operation;
import static com.gabriel.faclovers.production_service.TestFixtures.pieceModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.gabriel.faclovers.production_service.employee.EmployeeRepository;
import com.gabriel.faclovers.production_service.lot.LotRepository;
import com.gabriel.faclovers.production_service.operation.OperationByModelRepository;
import com.gabriel.faclovers.production_service.security.CurrentCompany;
import com.gabriel.faclovers.production_service.shared.BusinessException;
import com.gabriel.faclovers.production_service.shared.NotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductionLogServiceTest {

    @Mock
    private ProductionLogRepository productionLogRepository;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private OperationByModelRepository operationRepository;

    @Spy
    private ProductionLogMapper productionLogMapper;

    @Mock
    private CurrentCompany currentCompany;

    @InjectMocks
    private ProductionLogService productionLogService;

    @Test
    void shouldCreateLogWhenAllResourcesBelongToLoggedCompany() {
        var model = pieceModel(1L, 1L);
        var lot = lot(1L, 1L, model);
        var employee = employee(1L, 1L);
        var operation = operation(1L, 1L, model);
        when(currentCompany.id()).thenReturn(1L);
        when(lotRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(lot));
        when(employeeRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(employee));
        when(operationRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(operation));
        when(productionLogRepository.save(org.mockito.ArgumentMatchers.any(EmployeeProductionLog.class))).thenAnswer(invocation -> {
            EmployeeProductionLog log = invocation.getArgument(0);
            log.setId(1L);
            log.setCreatedAt(LocalDateTime.now());
            return log;
        });

        var request = new ProductionLogRequest(1L, 1L, 1L, LocalDateTime.of(2026, 5, 28, 10, 30), 50, "Pacote finalizado");
        ProductionLogResponse response = productionLogService.create(request);

        assertThat(response.companyId()).isEqualTo(1L);
        assertThat(response.quantityProduced()).isEqualTo(50);
    }

    @Test
    void shouldThrowWhenLotDoesNotBelongToLoggedCompany() {
        when(currentCompany.id()).thenReturn(1L);
        when(lotRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.empty());

        var request = new ProductionLogRequest(1L, 1L, 1L, null, 50, null);

        assertThatThrownBy(() -> productionLogService.create(request)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowWhenEmployeeDoesNotBelongToLoggedCompany() {
        var model = pieceModel(1L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(lotRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(lot(1L, 1L, model)));
        when(employeeRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.empty());

        var request = new ProductionLogRequest(1L, 1L, 1L, null, 50, null);

        assertThatThrownBy(() -> productionLogService.create(request)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowWhenOperationDoesNotBelongToLoggedCompany() {
        var model = pieceModel(1L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(lotRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(lot(1L, 1L, model)));
        when(employeeRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(employee(1L, 1L)));
        when(operationRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.empty());

        var request = new ProductionLogRequest(1L, 1L, 1L, null, 50, null);

        assertThatThrownBy(() -> productionLogService.create(request)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowWhenOperationDoesNotBelongToLotModel() {
        var lotModel = pieceModel(1L, 1L);
        var operationModel = pieceModel(2L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(lotRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(lot(1L, 1L, lotModel)));
        when(employeeRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(employee(1L, 1L)));
        when(operationRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(operation(1L, 1L, operationModel)));

        var request = new ProductionLogRequest(1L, 1L, 1L, null, 50, null);

        assertThatThrownBy(() -> productionLogService.create(request)).isInstanceOf(BusinessException.class);
    }
}
